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
import ac.hurley.codehub_kotlin.compose.view.*
import ac.hurley.codehub_kotlin.compose.viewmodel.REFRESH_STOP
import ac.hurley.model.room.entity.Article
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
        viewModel.getArticleList(isRefresh = false)
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
                    viewModel.onPageChanged(0)
                    viewModel.getBanner()
                    viewModel.getArticleList()
                    viewModel.onRefreshChanged(REFRESH_START)
                },
                onLoad = {
                    viewModel.onLoadStateChanged(REFRESH_START)
                    viewModel.onPageChanged((viewModel.page.value ?: 0) + 1)
                    viewModel.getArticleList()
                },
                content = {
                    when (bannerData) {
                        AppLoading -> {
                            LoadingContent()
                        }
                        is ReqError -> {
                            ErrorContent(article = {
                                viewModel.getArticleList()
                                viewModel.getBanner()
                            })
                        }
                        is ReqSuccess<*> -> {
                            val data = bannerData as ReqSuccess<List<Banner>>
                            // UI 界面
                            Column {
                                LazyRow {
                                    items(data.data) {
                                        BannerCard(
                                            it,
                                            actions.article,
                                            Modifier.padding(start = 16.dp, bottom = 8.dp)
                                        )
                                    }
                                }

                                when (articleData) {
                                    AppLoading -> {
                                        Spacer(modifier = Modifier.height(0.dp))
                                    }
                                    // 如果请求成功
                                    is ReqSuccess<*> -> {
                                        viewModel.onLoadStateChanged(REFRESH_STOP)
                                        viewModel.onRefreshChanged(REFRESH_STOP)
                                        val articleList = articleData as ReqSuccess<List<Article>>
                                        // 如果数据为空，就重新调用刷新布局
                                        if (articleList.data.isEmpty()) {
                                            return@SwipeToRefreshAndLoadLayout
                                        }
                                        LazyColumn(modifier = modifier, state = listState) {
                                            itemsIndexed(articleList.data) { index, article ->
                                                ArticleListItem(
                                                    article = article,
                                                    index = index,
                                                    toArticle = { urlArgs ->
                                                        actions.article(urlArgs)
                                                    })
                                            }
                                        }
                                    }
                                    // 如果请求失败
                                    is ReqError -> {
                                        Spacer(modifier = Modifier.height(0.dp))
                                        viewModel.onLoadStateChanged(REFRESH_STOP)
                                        viewModel.onRefreshChanged(REFRESH_STOP)
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