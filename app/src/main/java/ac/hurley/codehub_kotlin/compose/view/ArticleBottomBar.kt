package ac.hurley.codehub_kotlin.compose.view

import ac.hurley.codehub_kotlin.compose.repository.CollectRepository
import ac.hurley.model.room.entity.Article
import ac.hurley.util.util.showToast
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.IconButton
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ac.hurley.codehub_kotlin.R
import androidx.compose.material.Icon

@Composable
fun ArticleBottomBar(
    article: Article?,
    coroutineScope: CoroutineScope,
    collectRepository: CollectRepository,
) {

    var collectIcon by remember {
        mutableStateOf(if (article?.collect == false) Icons.Filled.FavoriteBorder else Icons.Filled.Favorite)
    }
    var loadState by remember { mutableStateOf(false) }

    Surface(elevation = 2.dp) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .height(56.dp)
                .fillMaxWidth()
        ) {

            // 收藏按钮
            IconButton(onClick = {
                collectIcon = if (collectIcon == Icons.Filled.Favorite) {
                    coroutineScope.launch(Dispatchers.IO) {
                        val cancelCollect = collectRepository.cancelCollect(article?.id ?: 0)
                        if (cancelCollect.errorCode == 0) {
                            withContext(Dispatchers.Main) {
                                showToast(R.string.cancel_collect_success)
                            }
                        } else {
                            showToast(R.string.cancel_collect_failed)
                        }
                    }
                    Icons.Filled.FavoriteBorder
                } else {
                    coroutineScope.launch {
                        val cancelCollect = collectRepository.collect(article?.id ?: 0)
                        if (cancelCollect.errorCode == 0) {
                            withContext(Dispatchers.Main) {
                                showToast(R.string.collect_success)
                            }
                        } else {
                            showToast(R.string.collect_failed)
                        }
                    }
                    Icons.Filled.Favorite
                }
            }) {
                Icon(
                    imageVector = collectIcon,
                    contentDescription = "收藏"
                )
            }
        }
    }
}