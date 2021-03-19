package ac.hurley.codehub_kotlin.compose.nav

import ac.hurley.codehub_kotlin.compose.MainActions
import ac.hurley.model.room.entity.Article
import ac.hurley.util.AppContext
import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import ac.hurley.codehub_kotlin.R
import ac.hurley.codehub_kotlin.compose.common.baselineHeight
import ac.hurley.codehub_kotlin.compose.viewmodel.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*

@Composable
fun UserPage(actions: MainActions, themeViewModel: ThemeViewModel) {
    val scrollState = rememberScrollState()
    Column(modifier = Modifier.fillMaxSize()) {
        BoxWithConstraints(modifier = Modifier.weight(1f)) {
            Surface {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(scrollState),
                ) {
                    UserInfo(
                        article = actions.article,
                        login = actions.login,
                        containerHeight = this@BoxWithConstraints.maxHeight
                    )
                }
            }
        }
    }
}

@Composable
private fun UserInfo(article: (Article) -> Unit, login: () -> Unit, containerHeight: Dp) {
    val viewModel: LoginViewModel = viewModel()
    val logoutState by viewModel.logoutState.observeAsState(LogoutDefault)
    Column {
        Spacer(modifier = Modifier.height(8.dp))

        when (logoutState) {
            LogoutDefault -> {
                NameAndPos(true, login)
            }
            LogoutFinish -> {
                NameAndPos(false, login)
            }
        }

        UserProperty(
            article1 = Article(title = "Blog", link = "https://blog.withh.life/"),
            article2 = article
        )

        UserProperty(
            article1 = Article(title = "Github", link = "https://github.com/HurleyJames23/"),
            article2 = article
        )

        // 如果已经登录，切换为注销登录按钮
        if (AppContext.isLogin) {
            Button(
                onClick = { viewModel.logout() },
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
            ) {
                Text(text = stringResource(id = R.string.logout))
            }
        }

        Spacer(modifier = Modifier.height((containerHeight - 320.dp).coerceAtLeast(0.dp)))
    }
}

@Composable
private fun NameAndPos(refresh: Boolean, login: () -> Unit) {
    Column(
        // 如果已登录
        modifier = if (AppContext.isLogin) {
            Modifier.padding(horizontal = 16.dp)
        } else {
            Modifier
                .padding(horizontal = 16.dp)
                .clickable { login() }
        }
    ) {
        Name(
            refresh = refresh,
            modifier = Modifier.baselineHeight(32.dp)
        )
        Position(
            refresh = refresh,
            modifier = Modifier
                .padding(bottom = 20.dp)
                .baselineHeight(24.dp)
        )
    }
}

@Composable
private fun Name(refresh: Boolean, modifier: Modifier = Modifier) {
    Text(
        text = if (AppContext.isLogin && refresh) AppContext.nickname else stringResource(id = R.string.not_login),
        modifier = modifier,
        style = MaterialTheme.typography.h5
    )
}

@Composable
private fun Position(refresh: Boolean, modifier: Modifier = Modifier) {
    CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
        Text(
            text = if (AppContext.isLogin && refresh) AppContext.username else stringResource(id = R.string.click_login),
            modifier = modifier, style = MaterialTheme.typography.body1
        )
    }
}

@Composable
fun UserProperty(article1: Article, article2: (Article) -> Unit) {
    Column(modifier = Modifier
        .clickable { article2(article1) }
        .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)) {
        Divider()
        CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
            Text(
                text = article1.title,
                modifier = Modifier.baselineHeight(24.dp),
                style = MaterialTheme.typography.caption
            )
        }
        val style = MaterialTheme.typography.body1
        Text(
            text = article1.title,
            modifier = Modifier.baselineHeight(24.dp),
            style = style
        )
    }
}

// TODO: Header
// TODO: Change Theme