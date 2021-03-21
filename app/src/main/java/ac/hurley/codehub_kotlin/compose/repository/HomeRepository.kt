package ac.hurley.codehub_kotlin.compose.repository

import ac.hurley.codehub_kotlin.compose.AppLoading
import ac.hurley.codehub_kotlin.compose.AppState
import ac.hurley.codehub_kotlin.compose.ReqError
import ac.hurley.codehub_kotlin.compose.ReqSuccess
import ac.hurley.model.AppDatabase
import ac.hurley.model.room.dao.BannerDao
import ac.hurley.model.room.entity.Banner
import ac.hurley.net.base.CodeHubNetwork
import ac.hurley.net.base.DOWNLOAD_IMAGE_TIME
import ac.hurley.net.base.DOWNLOAD_OFFICIAL_ACCOUNT_ARTICLE_TIME
import ac.hurley.net.base.ONE_DAY
import ac.hurley.net.download.DownloadBuild
import ac.hurley.net.download.DownloadImpl
import ac.hurley.net.download.DownloadStatus
import ac.hurley.util.util.DataStoreUtils
import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.blankj.utilcode.util.LogUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.lang.RuntimeException

class HomeRepository constructor(val application: Application) {
    companion object {
        private const val TAG = "HomeRepository"
    }

    suspend fun getBanner(state: MutableLiveData<AppState>) {
        state.postValue(AppLoading)
        val dataStore = DataStoreUtils
        var downloadImageTime = 0L
        dataStore.readLongFlow(DOWNLOAD_IMAGE_TIME, System.currentTimeMillis()).first {
            downloadImageTime = it
            true
        }
        // 从数据库中获取轮播图列表
        val bannerDao = AppDatabase.getDatabase(application).bannerDao()
        val bannerList = bannerDao.getBannerList()
        if (bannerList.isNotEmpty() && downloadImageTime > 0 && downloadImageTime - System.currentTimeMillis() < ONE_DAY) {
            state.postValue(ReqSuccess(bannerList))
        } else {
            // 网络请求获取列表
            val bannerResponse = CodeHubNetwork.getBanner()
            if (bannerResponse.errorCode == 0) {
                val banners = bannerResponse.data
                // 网络请求获取的和数据库中已有的一致
                if (bannerList.isNotEmpty() && bannerList[0].url == banners[0].url) {
                    state.postValue(ReqSuccess(bannerList))
                } else {
                    // 删除数据库中保存的所有轮播图
                    bannerDao.deleteAllBanners()
                    addBannerList(bannerDao, banners)
                    state.postValue(ReqSuccess(banners))
                }
                dataStore.saveLongData(DOWNLOAD_IMAGE_TIME, System.currentTimeMillis())
            } else {
                // 网络请求失败
                state.postValue(
                    ReqError(
                        RuntimeException(
                            "Response Status is ${bannerResponse.errorCode}   " +
                                    "msg is ${bannerResponse.errorMsg}"
                        )
                    )
                )
            }
        }
    }

    private suspend fun addBannerList(
        bannerDao: BannerDao, bannerList: List<Banner>
    ) {
        val uiScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
        bannerList.forEach {
            DownloadImpl.download(it.imagePath, DownloadBuild(application)).collect { value ->
                when (value) {
                    // 下载请求出错
                    is DownloadStatus.DownloadError -> {
                        LogUtils.e("emit: error: ${value.t}")
                    }
                    // 下载请求成功
                    is DownloadStatus.DownloadSuccess -> {
                        LogUtils.d("addBannerList: ${value.file?.path}")
                        // 下载完成
                        uiScope.launch(Dispatchers.IO) {
                            val banner = bannerDao.loadBanner(it.id)
                            if (banner == null) {
                                it.filePath = value.file?.path ?: ""
                                bannerDao.addBanner(it)
                            }
                        }
                    }
                    // 下载过程中
                    is DownloadStatus.DownloadProcess -> {

                    }
                }
            }
        }
    }
}