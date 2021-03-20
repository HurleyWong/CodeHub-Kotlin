package ac.hurley.codehub_kotlin.compose.page

import ac.hurley.codehub_kotlin.compose.AppLoading
import ac.hurley.codehub_kotlin.compose.ReqError
import ac.hurley.codehub_kotlin.compose.ReqSuccess
import ac.hurley.codehub_kotlin.compose.view.ErrorContent
import ac.hurley.codehub_kotlin.compose.view.LoadingContent
import ac.hurley.codehub_kotlin.compose.viewmodel.BasePageViewModel
import ac.hurley.codehub_kotlin.compose.viewmodel.REFRESH_START
import ac.hurley.model.room.entity.Article
import ac.hurley.model.room.entity.ProjectClassify
import ac.hurley.net.base.QueryArticle
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ScrollableTabRow
import androidx.compose.material.Tab
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.chrisbanes.accompanist.insets.statusBarsHeight

@Composable
fun ArticleListPage(
    article: (Article) -> Unit,
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
                        val data = result as ReqSuccess<List<ProjectClassify>>
                        ScrollableTabRow(
                            selectedTabIndex = position ?: 0,
                            modifier = Modifier.wrapContentWidth(),
                            edgePadding = 3.dp
                        ) {
                            data.data.forEachIndexed { index, projectClassify ->
                                Tab(
                                    text = { Text(projectClassify.name) },
                                    selected = position == index,
                                    onClick = {
                                        articleViewModel.getDataList(
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
                                        articleViewModel.page.value ?: 0,
                                        data.data[0].id,
                                        true
                                    )
                                )
                            }
                        }


                    }
                }
            }
        }
    }

}