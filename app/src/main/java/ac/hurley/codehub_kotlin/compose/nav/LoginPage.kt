package ac.hurley.codehub_kotlin.compose.nav

import ac.hurley.codehub_kotlin.R
import ac.hurley.codehub_kotlin.compose.MainActions
import ac.hurley.codehub_kotlin.compose.login.*
import ac.hurley.codehub_kotlin.compose.theme.snackBarAction
import ac.hurley.codehub_kotlin.compose.viewmodel.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch

@Composable
fun LoginPage(onNavigationEvent: MainActions, viewModel: LoginViewModel = viewModel()) {
    val state by viewModel.state.observeAsState()

    Login(onNavigationEvent = { event ->
        when (event) {
            is LoginEvent.Login -> {
                viewModel.loginOrRegister(Account(event.email, event.password, true))
            }
            LoginEvent.Register -> {
                viewModel.logout()
            }
            LoginEvent.LoginAsGuest -> {
                viewModel.logout()
            }
            LoginEvent.NavBack -> {
                onNavigationEvent.upPress()
            }
        }
    })

    when (state) {
        Logging -> {

        }
        is LoginSuccess -> {
            onNavigationEvent.homePage()
        }
        LoginError -> {

        }
    }
}

@Composable
fun Login(onNavigationEvent: (LoginEvent) -> Unit) {

    /**
     * SnackBar
     */
    val snackBarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    /**
     * SnackBar 提示错误信息
     */
    val snackBarErrorText = stringResource(id = R.string.unavailable)
    val snackBarActionLabel = stringResource(id = R.string.dismiss)

    Scaffold(
        // 顶部导航栏
        topBar = {
            ac.hurley.codehub_kotlin.compose.common.AppBar(
                title = stringResource(id = R.string.login),
                click = {
                    onNavigationEvent(LoginEvent.NavBack)
                })
        },
        content = {
            LoginInput(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    LoginContent(onLoginSubmitted = { email, password ->
                        onNavigationEvent(LoginEvent.Login(email, password))
                    })

                    Spacer(modifier = Modifier.height(16.dp))
                    TextButton(
                        onClick = {
                            scope.launch {
                                snackBarHostState.showSnackbar(
                                    message = snackBarErrorText,
                                    actionLabel = snackBarActionLabel
                                )
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = stringResource(id = R.string.forget_password))
                    }
                }
            }
        }
    )

    Box(modifier = Modifier.fillMaxSize()) {
        SnackBarError(
            snackbarHostState = snackBarHostState,
            onDismiss = {
                snackBarHostState.currentSnackbarData?.dismiss()
            },
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

@Composable
fun LoginContent(onLoginSubmitted: (email: String, password: String) -> Unit) {
    Column(modifier = Modifier.fillMaxWidth()) {
        val focusRequester = remember {
            FocusRequester()
        }
        val emailState = remember {
            EmailState()
        }

        // 邮箱行
        Email(emailState = emailState, onImeAction = { focusRequester.requestFocus() })

        Spacer(modifier = Modifier.height(16.dp))

        val passwordState = remember { PasswordState() }

        // 密码行
        Password(
            label = stringResource(id = R.string.password),
            passwordState = passwordState,
            modifier = Modifier.focusRequester(focusRequester = focusRequester),
            onImeAction = {
                onLoginSubmitted(emailState.text, passwordState.text)
            })
        Spacer(modifier = Modifier.height(16.dp))

        // 登录按钮
        Button(
            onClick = {
                onLoginSubmitted(emailState.text, passwordState.text)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp), enabled = emailState.isValid && passwordState.isValid
        ) {
            Text(text = stringResource(id = R.string.login))
        }

    }
}

/**
 * 提示错误信息的 SnackBar
 */
@Composable
fun SnackBarError(
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit = {}
) {
    SnackbarHost(
        hostState = snackbarHostState, snackbar = { data ->
            Snackbar(
                modifier = Modifier.padding(16.dp),
                content = {
                    Text(
                        text = data.message,
                        style = MaterialTheme.typography.body2
                    )
                },
                action = {
                    data.actionLabel?.let {
                        TextButton(onClick = onDismiss) {
                            Text(
                                text = stringResource(id = R.string.dismiss),
                                color = MaterialTheme.colors.snackBarAction
                            )
                        }
                    }
                },
            )
        },
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight(Alignment.Bottom)
    )
}


sealed class LoginEvent {
    data class Login(val email: String, val password: String) : LoginEvent()
    object Register : LoginEvent()
    object LoginAsGuest : LoginEvent()
    object NavBack : LoginEvent()
}