package ac.hurley.codehub_kotlin.compose.page

import ac.hurley.codehub_kotlin.compose.AppLoading
import ac.hurley.codehub_kotlin.compose.MainActions
import ac.hurley.codehub_kotlin.compose.common.AppBar
import ac.hurley.codehub_kotlin.compose.viewmodel.HomeViewModel
import ac.hurley.codehub_kotlin.compose.viewmodel.REFRESH_START
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import ac.hurley.codehub_kotlin.R
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search

@Composable
fun HomePage(
    actions: MainActions,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = viewModel()
) {
    val coroutineScope = rememberCoroutineScope()

    val articleData by viewModel.state.observeAsState(AppLoading)

    val bannerData by viewModel.bannerState.observeAsState(AppLoading)

    val refresh by viewModel.refreshState.observeAsState()

    val load by viewModel.loadState.observeAsState()

    /**
     * 如果既没有下拉刷新，也没有上拉加载更多
     */
    if (refresh != REFRESH_START && load != REFRESH_START) {

    }

    /**
     * 绘制 UI 界面
     */
    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            AppBar(
                title = stringResource(id = R.string.homepage),
                false,
                showRight = true,
                rightImg = Icons.Rounded.Search,
                rightClick = actions.search
            )
        }
    }
}