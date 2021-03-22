package ac.hurley.codehub_kotlin.compose.repository

import ac.hurley.codehub_kotlin.compose.AppLoading
import ac.hurley.codehub_kotlin.compose.AppState
import ac.hurley.codehub_kotlin.compose.ReqError
import ac.hurley.codehub_kotlin.compose.ReqSuccess
import ac.hurley.model.AppDatabase
import ac.hurley.model.room.dao.BannerDao
import ac.hurley.model.room.entity.Article
import ac.hurley.model.room.entity.Banner
import ac.hurley.model.room.entity.HOME
import ac.hurley.model.room.entity.HOME_TOP
import ac.hurley.net.base.*
import ac.hurley.net.download.DownloadBuild
import ac.hurley.net.download.DownloadImpl
import ac.hurley.net.download.DownloadStatus
import ac.hurley.util.util.DataStoreUtils
import android.accounts.NetworkErrorException
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
                    // 将网络请求获得的轮播图添加到数据库中
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

    /**
     * 将请求得到的轮播图添加到数据库中
     */
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
                                // 将请求到的数据添加到 Room 数据库中
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

    /**
     * 请求文章列表
     */
    suspend fun getArticleList(
        state: MutableLiveData<AppState>,
        value: MutableLiveData<ArrayList<Article>>,
        query: QueryHomeArticle,
        isLoad: Boolean,
    ) {
        // 如果没有加载更多
        if (!isLoad) {
            state.postValue(AppLoading)
        }
        val res: ArrayList<Article>

        if (query.page == 0) {
            res = arrayListOf()
            val dataStore = DataStoreUtils
            var downloadArticleTime = 0L
            dataStore.readLongFlow(DOWNLOAD_ARTICLE_TIME, System.currentTimeMillis()).first {
                downloadArticleTime = it
                true
            }
            val articleListDao = AppDatabase.getDatabase(application).articleDao()
            val articleListHome = articleListDao.getArticleList(HOME)
            val articleListTop = articleListDao.getTopArticleList(HOME_TOP)
            var downloadTopArticleTime = 0L
            dataStore.readLongFlow(DOWNLOAD_TOP_ARTICLE_TIME, System.currentTimeMillis()).first {
                downloadTopArticleTime = it
                true
            }
            // 如果置顶文章不为空，并且下载时间小于 4 小时，并且未刷新
            if (articleListTop.isNotEmpty() && downloadTopArticleTime > 0 &&
                downloadTopArticleTime - System.currentTimeMillis() < FOUR_HOUR && !query.isRefresh
            ) {
                res.addAll(articleListTop)
            } else {
                // 从网络请求文章列表
                val articleListTopDeferred = CodeHubNetwork.getTopArticleList()
                if (articleListTopDeferred.errorCode == 0) {
                    // 如果网络请求获得的文章与数据库中的一致，则直接添加数据库中的即可
                    if (articleListTop.isNotEmpty() && articleListTop[0].link == articleListTopDeferred.data[0].link
                        && !query.isRefresh
                    ) {
                        res.addAll(articleListTop)
                    } else {
                        res.addAll(articleListTopDeferred.data)
                        articleListTopDeferred.data.forEach {
                            it.localType = HOME_TOP
                        }
                        dataStore.saveLongData(
                            DOWNLOAD_TOP_ARTICLE_TIME,
                            System.currentTimeMillis()
                        )
                        // 删除掉数据库中已有的文章数据
                        articleListDao.deleteAllArticles(HOME_TOP)
                        articleListDao.addArticleList(articleListTopDeferred.data)
                    }
                }
            }

            // 逻辑与获取置顶文章一致，只不过是换成了 getHomeArticleList
            if (articleListHome.isNotEmpty() && downloadArticleTime > 0 && downloadArticleTime - System.currentTimeMillis() < FOUR_HOUR
                && !query.isRefresh
            ) {
                res.addAll(articleListHome)
                state.postValue(ReqSuccess<List<Article>>(res))
                value.postValue(res)
            } else {
                val articleListDefered = CodeHubNetwork.getArticleList((query.page))
                if (articleListDefered.errorCode == 0) {
                    if (articleListHome.isNotEmpty() && articleListHome[0].link == articleListDefered.data.datas[0].link
                        && !query.isRefresh
                    ) {
                        res.addAll(articleListHome)
                    } else {
                        res.addAll(articleListDefered.data.datas)
                        articleListDefered.data.datas.forEach {
                            it.localType = HOME
                        }
                        dataStore.saveLongData(DOWNLOAD_ARTICLE_TIME, System.currentTimeMillis())
                        articleListDao.deleteAllArticles(HOME)
                        articleListDao.addArticleList(articleListDefered.data.datas)
                    }
                    state.postValue(ReqSuccess<List<Article>>(res))
                    value.postValue(res)
                } else {
                    state.postValue(ReqError(NetworkErrorException("网络连接错误")))
                }
            }

        } else {
            res = value.value ?: arrayListOf()
            val articleListDeferred = CodeHubNetwork.getArticleList(query.page)
            if (articleListDeferred.errorCode == 0) {
                res.addAll(articleListDeferred.data.datas)
                state.postValue(ReqSuccess<List<Article>>(res))
                value.postValue(res)
            } else {
                state.postValue(ReqError(NetworkErrorException("网络错误")))
            }
        }


    }
}