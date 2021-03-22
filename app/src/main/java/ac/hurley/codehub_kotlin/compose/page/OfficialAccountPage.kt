package ac.hurley.codehub_kotlin.compose.page

import ac.hurley.codehub_kotlin.compose.common.article.ArticlePage
import ac.hurley.codehub_kotlin.compose.viewmodel.OfficialAccountListViewModel
import ac.hurley.codehub_kotlin.compose.viewmodel.OfficialAccountViewModel
import ac.hurley.model.room.entity.Article
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun OfficialAccountPage(article: (Article) -> Unit, modifier: Modifier = Modifier) {
    val viewModel: OfficialAccountViewModel = viewModel()
    val officialListViewModel: OfficialAccountListViewModel = viewModel()

    ClassifyListPage(
        toArticle = article,
        modifier = modifier,
        viewModel = viewModel,
        articleViewModel = officialListViewModel
    )
}