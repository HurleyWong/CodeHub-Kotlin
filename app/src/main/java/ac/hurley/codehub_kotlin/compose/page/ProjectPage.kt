package ac.hurley.codehub_kotlin.compose.page

import ac.hurley.codehub_kotlin.compose.viewmodel.ProjectListViewModel
import ac.hurley.codehub_kotlin.compose.viewmodel.ProjectViewModel
import ac.hurley.model.room.entity.Article
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun ProjectPage(toArticle: (Article) -> Unit, modifier: Modifier = Modifier) {
    val viewModel: ProjectViewModel = viewModel()
    val projectViewModel: ProjectListViewModel = viewModel()

    ClassifyListPage(toArticle = toArticle, modifier = modifier, viewModel = viewModel, articleViewModel = projectViewModel)

}