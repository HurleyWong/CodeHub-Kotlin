package ac.hurley.codehub_kotlin.compose

import ac.hurley.codehub_kotlin.R
import ac.hurley.codehub_kotlin.compose.nav.HomePage
import ac.hurley.codehub_kotlin.compose.nav.OfficialAccountPage
import ac.hurley.codehub_kotlin.compose.nav.ProjectPage
import ac.hurley.codehub_kotlin.compose.nav.UserPage
import ac.hurley.codehub_kotlin.compose.viewmodel.HomeViewModel
import ac.hurley.codehub_kotlin.compose.viewmodel.ThemeViewModel
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import dev.chrisbanes.accompanist.insets.navigationBarsHeight
import dev.chrisbanes.accompanist.insets.navigationBarsPadding
import java.util.*

@Composable
fun Main(actions: MainActions, themeViewModel: ThemeViewModel) {
    val viewModel: HomeViewModel = viewModel()
    val position by viewModel.position.observeAsState()
    val tabs = Tabs.values()

    // 定义 UI 界面
    Scaffold(
        backgroundColor = colorResource(id = R.color.blue),
        bottomBar = {
            BottomNavigation(
                Modifier.navigationBarsHeight(additional = 56.dp)
            ) {
                tabs.forEach { tab ->
                    BottomNavigationItem(
                        modifier = Modifier
                            .background(MaterialTheme.colors.primary)
                            .navigationBarsPadding(),
                        icon = {
                            Icon(painterResource(tab.icon), contentDescription = null)
                        },
                        label = {
                            Text(stringResource(tab.title).toUpperCase(Locale.ROOT))
                        },
                        selected = tab == position,
                        onClick = {
                            viewModel.onPositionChanged(tab)
                        },
                        alwaysShowLabel = false,
                    )
                }
            }
        }
    ) { innerPadding ->
        val modifier = Modifier.padding(innerPadding)
        when (position) {
            Tabs.HOMEPAGE -> HomePage(
                actions = actions,
                modifier = Modifier,
                viewModel = viewModel()
            )
            Tabs.PROJECT -> ProjectPage(actions.article, modifier)
            Tabs.OFFICIAL_ACCOUNT -> OfficialAccountPage(actions.article, modifier)
            Tabs.USER -> UserPage(actions = actions, themeViewModel = themeViewModel)
        }
    }
}

/**
 * 自定义一个枚举类，包含底部 Nav 栏的 title 和 icon
 */
enum class Tabs(@StringRes val title: Int, @DrawableRes val icon: Int) {
    HOMEPAGE(R.string.homepage, R.drawable.ic_nav_user_normal),
    PROJECT(R.string.project, R.drawable.ic_nav_project_normal),
    OFFICIAL_ACCOUNT(R.string.official_account, R.drawable.ic_nav_wechat_normal),
    USER(R.string.user, R.drawable.ic_nav_user_normal)
}