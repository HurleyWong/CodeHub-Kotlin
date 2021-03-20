package ac.hurley.codehub_kotlin.compose.viewmodel

import ac.hurley.codehub_kotlin.compose.AppState
import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * 包含列表分页的 ViewModel
 */
abstract class BasePageViewModel<Key>(application: Application) :
    BaseViewModel(application = application) {

    private val _page = MutableLiveData<Int>()

    private val _position = MutableLiveData(0)

    val page: LiveData<Int>
        get() = _page

    val position: LiveData<Int> = _position

    fun onPageChanged(refresh: Int) {
        _page.postValue(refresh)
    }

    fun onPositionChanged(position: Int) {
        _position.value = position
    }

    private val pageLiveData = MutableLiveData<Key>()

    val dataLiveData: LiveData<AppState>
        get() = mutableLiveData

    protected val mutableLiveData = MutableLiveData<AppState>()

    abstract suspend fun getData(page: Key)

    fun getDataList(page: Key) {
        viewModelScope.launch(Dispatchers.IO) { getData(page) }
    }
}