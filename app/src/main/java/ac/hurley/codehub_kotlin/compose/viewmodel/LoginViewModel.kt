package ac.hurley.codehub_kotlin.compose.viewmodel

import ac.hurley.codehub_kotlin.compose.repository.AccountRepository
import ac.hurley.model.model.LoginModel
import ac.hurley.util.AppContext
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val accountRepository: AccountRepository = AccountRepository()

    // 登录的状态
    private val _state = MutableLiveData<LoginState>()
    val state: LiveData<LoginState>
        get() = _state

    // 注销登录状态
    private val _logoutState = MutableLiveData<LogoutState>()
    val logoutState: LiveData<LogoutState>
        get() = _logoutState

    fun logout() {
        viewModelScope.launch {
            accountRepository.getLogout()
            AppContext.logout()
            _logoutState.postValue(LogoutFinish)
        }
    }
}


sealed class LoginState
object Logging : LoginState()
data class LoginSuccess(val login: LoginModel) : LoginState()
object LoginError : LoginState()

sealed class LogoutState
object LogoutFinish : LogoutState()
object LogoutDefault : LogoutState()