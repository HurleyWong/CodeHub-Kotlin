package ac.hurley.codehub_kotlin.compose.viewmodel

import ac.hurley.codehub_kotlin.compose.AppState
import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class HomeViewModel(application: Application) : BaseViewModel(application = application) {

    private val _state = MutableLiveData<AppState>()

    val state: LiveData<AppState>
        get() = _state

    private val _bannerState = MutableLiveData<AppState>()

    val bannerState: LiveData<AppState>
        get() = _bannerState
}