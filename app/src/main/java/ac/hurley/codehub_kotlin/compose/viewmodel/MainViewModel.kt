package ac.hurley.codehub_kotlin.compose.viewmodel

import ac.hurley.codehub_kotlin.compose.Tabs
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {

    private val _position = MutableLiveData(Tabs.HOMEPAGE)
    val position: LiveData<Tabs> = _position

    fun onPositionChanged(position: Tabs) {
        _position.value = position
    }
}