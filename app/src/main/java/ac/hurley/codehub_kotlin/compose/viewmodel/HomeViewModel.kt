package ac.hurley.codehub_kotlin.compose.viewmodel

import ac.hurley.codehub_kotlin.compose.AppState
import ac.hurley.codehub_kotlin.compose.repository.HomeRepository
import ac.hurley.model.room.entity.Article
import ac.hurley.net.base.QueryHomeArticle
import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel(application: Application) : BaseViewModel(application = application) {

    private val homeRepository: HomeRepository = HomeRepository(application = application)

    private val _state = MutableLiveData<AppState>()

    val state: LiveData<AppState>
        get() = _state

    private val _bannerState = MutableLiveData<AppState>()

    val bannerState: LiveData<AppState>
        get() = _bannerState

    private val _articleDataList = MutableLiveData<ArrayList<Article>>()

    private val _page = MutableLiveData<Int>()

    val page: LiveData<Int>
        get() = _page

    fun onPageChanged(refresh: Int) {
        _page.postValue(refresh)
    }

    /**
     * 获取轮播图
     */
    fun getBanner() {
        viewModelScope.launch(Dispatchers.IO) {
            homeRepository.getBanner(_bannerState)
        }
    }

    /**
     * 获取文章列表
     */
    fun getArticleList(isLoad: Boolean = false, isRefresh: Boolean = true) {
        viewModelScope.launch(Dispatchers.IO) {
            homeRepository.getArticleList(
                _state, _articleDataList, QueryHomeArticle(page.value ?: 0, isRefresh = isRefresh),
                isLoad = isLoad
            )
        }
    }
}