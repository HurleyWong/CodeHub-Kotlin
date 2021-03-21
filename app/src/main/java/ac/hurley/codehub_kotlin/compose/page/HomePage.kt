package ac.hurley.codehub_kotlin.compose.page

import ac.hurley.codehub_kotlin.compose.AppLoading
import ac.hurley.codehub_kotlin.compose.MainActions
import ac.hurley.codehub_kotlin.compose.common.AppBar
import ac.hurley.codehub_kotlin.compose.viewmodel.HomeViewModel
import ac.hurley.codehub_kotlin.compose.viewmodel.REFRESH_START
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import ac.hurley.codehub_kotlin.R
import ac.hurley.codehub_kotlin.compose.ReqError
import ac.hurley.codehub_kotlin.compose.ReqSuccess
import ac.hurley.codehub_kotlin.compose.view.BannerCard
import ac.hurley.codehub_kotlin.compose.view.ErrorContent
import ac.hurley.codehub_kotlin.compose.view.LoadingContent
import ac.hurley.codehub_kotlin.compose.view.SwipeToRefreshAndLoadLayout
import ac.hurley.model.room.entity.Banner
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.ui.unit.dp

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
        viewModel.getBanner()
    }

    var listState = rememberLazyListState()

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

            SwipeToRefreshAndLoadLayout(
                stateOfRefresh = refresh == REFRESH_START,
                stateOfLoad = load == REFRESH_START,
                onRefresh = {
                    viewModel.getBanner()
                    // TODO
                    viewModel.onRefreshChanged(REFRESH_START)
                },
                onLoad = {
                    viewModel.onLoadStateChanged(REFRESH_START)
                    viewModel.onPageChanged((viewModel.page.value ?: 0) + 1)
                    // TODO
                },
                content = {
                    when (bannerData) {
                        AppLoading -> {
                            LoadingContent()
                        }
                        is ReqError -> {
                            ErrorContent(article = {
                                // TODO
                                viewModel.getBanner()
                            })
                        }
                        is ReqSuccess<*> -> {
                            val data = bannerData as ReqSuccess<List<Banner>>
                            Column {
                                LazyRow(modifier = Modifier.padding(end = 16.dp)) {
                                    items(data.data) {
                                        BannerCard(
                                            it,
                                            actions.article,
                                            Modifier.padding(start = 16.dp, bottom = 16.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            )
        }
    }
}