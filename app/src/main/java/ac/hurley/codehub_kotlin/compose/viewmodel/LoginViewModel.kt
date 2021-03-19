package ac.hurley.codehub_kotlin.compose.viewmodel

import ac.hurley.codehub_kotlin.R
import ac.hurley.codehub_kotlin.compose.repository.AccountRepository
import ac.hurley.model.model.BaseModel
import ac.hurley.model.model.LoginModel
import ac.hurley.util.AppContext
import ac.hurley.util.util.showToast
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val accountRepository: AccountRepository = AccountRepository()

    /**
     * 登录或者注册
     */
    fun loginOrRegister(account: Account) {
        _state.postValue(Logging)
        viewModelScope.launch(Dispatchers.IO) {
            val loginModel: BaseModel<LoginModel> = if (account.isLogin) {
                accountRepository.getLogin(account.username, account.password)
            } else {
                accountRepository.getRegister(
                    account.username,
                    account.password,
                    account.password
                )
            }

            // 如果没有报错
            if (loginModel.errorCode == 0) {
                val login = loginModel.data
                _state.postValue(LoginSuccess(login))
                AppContext.isLogin = true
                AppContext.setUserInfo(nickname = login.nickname, username = login.username)
                showToast(
                    // 账号已登录，则登录成功
                    if (account.isLogin) {
                        getApplication<Application>().getString(R.string.login_success)
                    } else {
                        // 否则，注册成功
                        getApplication<Application>().getString(R.string.register_success)
                    }
                )
            } else {
                showToast(loginModel.errorMsg)
                _state.postValue(LoginError)
            }
        }
    }

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

data class Account(val username: String, val password: String, val isLogin: Boolean)