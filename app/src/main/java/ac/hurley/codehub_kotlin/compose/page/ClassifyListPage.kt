package ac.hurley.codehub_kotlin.compose.page

import ac.hurley.codehub_kotlin.R
import ac.hurley.codehub_kotlin.compose.AppLoading
import ac.hurley.codehub_kotlin.compose.ReqError
import ac.hurley.codehub_kotlin.compose.ReqSuccess
import ac.hurley.codehub_kotlin.compose.view.ArticleListItem
import ac.hurley.codehub_kotlin.compose.view.ErrorContent
import ac.hurley.codehub_kotlin.compose.view.LoadingContent
import ac.hurley.codehub_kotlin.compose.view.SwipeToRefreshAndLoadLayout
import ac.hurley.codehub_kotlin.compose.viewmodel.BasePageViewModel
import ac.hurley.codehub_kotlin.compose.viewmodel.REFRESH_START
import ac.hurley.codehub_kotlin.compose.viewmodel.REFRESH_STOP
import ac.hurley.model.room.entity.Article
import ac.hurley.model.room.entity.Classify
import ac.hurley.net.base.QueryArticle
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import dev.chrisbanes.accompanist.insets.statusBarsHeight
import kotlinx.coroutines.launch

private const val TAG = "ClassifyListPage"

@Composable
fun ClassifyListPage(
    toArticle: (Article) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: BasePageViewModel<Boolean>,
    articleViewModel: BasePageViewModel<QueryArticle>
) {
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    var loadState by remember { mutableStateOf(false) }
    var loadPageState by remember { mutableStateOf(false) }
    val refresh by viewModel.refreshState.observeAsState()
    val load by viewModel.loadState.observeAsState()

    if (!loadState && refresh != REFRESH_START && load != REFRESH_START) {
        viewModel.getDataList(false)
    }

    val result by viewModel.dataLiveData.observeAsState(AppLoading)
    val position by viewModel.position.observeAsState()
    val articleList by articleViewModel.dataLiveData.observeAsState(AppLoading)

    Box(modifier = Modifier.fillMaxSize()) {
        Column {
            Column(modifier = Modifier.background(color = MaterialTheme.colors.primary)) {
                Spacer(modifier = Modifier.statusBarsHeight())

                when (result) {
                    is ReqError -> {
                        ErrorContent(article = {
                            viewModel.getDataList(false)
                        })
                        loadState = true
                    }
                    AppLoading -> {
                        LoadingContent()
                    }
                    is ReqSuccess<*> -> {
                        loadState = true
                        val data = result as ReqSuccess<List<Classify>>
                        ScrollableTabRow(
                            selectedTabIndex = position ?: 0,
                            modifier = Modifier.wrapContentWidth(),
                            edgePadding = 3.dp
                        ) {
                            data.data.forEachIndexed { index, projectClassify ->
                                // Tab 栏显示具体的分类
                                Tab(
                                    text = { Text(projectClassify.name) },
                                    selected = position == index,
                                    onClick = {
                                        articleViewModel.getDataList(
                                            // 根据分类 id，查询该分类下的文章列表
                                            QueryArticle(
                                                0,
                                                projectClassify.id,
                                                true
                                            )
                                        )
                                        viewModel.onPositionChanged(index)
                                    }
                                )
                            }

                            if (position == 0 && !loadPageState) {
                                articleViewModel.getDataList(
                                    QueryArticle(
                                        // 如果 value 不为空，就等于 value；如果为空，则等于 0
                                        articleViewModel.page.value ?: 0,
                                        data.data[0].id,
                                        true
                                    )
                                )
                            }
                        }

                        SwipeToRefreshAndLoadLayout(
                            stateOfRefresh = refresh == REFRESH_START,
                            stateOfLoad = load == REFRESH_START,
                            onRefresh = {
                                viewModel.onPageChanged(0)
                                viewModel.onRefreshChanged(REFRESH_START)
                                Log.d(TAG, "onRefresh: 开始刷新")
                                articleViewModel.getDataList(
                                    QueryArticle(0, data.data[position ?: 0].id, false)
                                )
                            },
                            onLoad = {
                                viewModel.onPageChanged((viewModel.page.value ?: 1) + 1)
                                viewModel.onLoadStateChanged(REFRESH_START)
                                articleViewModel.getDataList(
                                    QueryArticle(
                                        viewModel.page.value ?: 0,
                                        data.data[position ?: 0].id,
                                        true
                                    )
                                )
                            },
                            content = {
                                when (articleList) {
                                    is AppLoading -> {
                                        LoadingContent()
                                        viewModel.onRefreshChanged(REFRESH_STOP)
                                    }
                                    is ReqSuccess<*> -> {
                                        viewModel.onRefreshChanged(REFRESH_STOP)
                                        viewModel.onLoadStateChanged(REFRESH_STOP)
                                        loadPageState = true
                                        val articles = articleList as ReqSuccess<List<Article>>
                                        LazyColumn(modifier, listState) {
                                            itemsIndexed(articles.data) { index, article ->
                                                ArticleListItem(
                                                    article = article,
                                                    index = index,
                                                    toArticle = toArticle
                                                )
                                            }
                                        }
                                    }
                                    is ReqError -> {
                                        viewModel.onLoadStateChanged(REFRESH_STOP)
                                        viewModel.onRefreshChanged(REFRESH_STOP)
                                        ErrorContent(article = {
                                            articleViewModel.getDataList(
                                                QueryArticle(0, data.data[position ?: 0].id, true)
                                            )
                                        })
                                        loadPageState = true
                                    }
                                }
                            }
                        )
                    }
                }
            }
        }

        Button(modifier = Modifier
            .padding(bottom = 65.dp, end = 8.dp)
            .align(Alignment.BottomEnd), onClick = {
            coroutineScope.launch {
                listState.animateScrollToItem(index = 0)
            }
        }) {
            Image(
                painter = painterResource(id = R.drawable.img_to_top_normal),
                contentDescription = ""
            )
        }
    }

}