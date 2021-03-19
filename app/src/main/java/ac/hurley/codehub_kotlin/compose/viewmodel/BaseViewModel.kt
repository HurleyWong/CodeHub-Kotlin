package ac.hurley.codehub_kotlin.compose.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

/**
 * ViewModel 的抽象基类，包含各种通用的 ViewModel 方法
 */
abstract class BaseViewModel(application: Application) : AndroidViewModel(application) {
    private val _refreshState = MutableLiveData<Int>()

    val refreshState: LiveData<Int>
        get() = _refreshState

    fun onRefreshChanged(refresh: Int) {
        _refreshState.postValue(refresh)
    }

    private val _loadState = MutableLiveData(REFRESH_STOP)

    val loadState: LiveData<Int>
        get() = _loadState

    fun onLoadStateChanged(refresh: Int) {
        _loadState.postValue(refresh)
    }

}


/**
 * 刷新开始
 */
const val REFRESH_START = 1

/**
 * 刷新停止
 */
const val REFRESH_STOP = 2
