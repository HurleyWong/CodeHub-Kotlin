package ac.hurley.codehub_kotlin.compose.common.article

import ac.hurley.codehub_kotlin.R
import ac.hurley.codehub_kotlin.compose.common.rememberX5WebViewWithLifecycle
import ac.hurley.codehub_kotlin.compose.repository.CollectRepository
import ac.hurley.codehub_kotlin.compose.view.ArticleBottomBar
import ac.hurley.model.room.entity.Article
import ac.hurley.util.util.getHtmlText
import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.blankj.utilcode.util.StringUtils
import kotlinx.coroutines.coroutineScope

@Composable
fun ArticlePage(article: Article?, onBack: () -> Unit) {
    ArticleContent(article = article, onBack = onBack)
}

@Composable
fun ArticleContent(article: Article?, onBack: () -> Unit) {
    var showDialog by rememberSaveable {
        mutableStateOf(false)
    }
    if (showDialog) {
        FunctionalityNotAvailablePopup {
            showDialog = false
        }
    }

    val x5WebView = rememberX5WebViewWithLifecycle()
    val coroutineScope = rememberCoroutineScope()
    val collectRepository = CollectRepository()

    Scaffold(
        topBar = {
            ac.hurley.codehub_kotlin.compose.common.AppBar(
                title = getHtmlText(
                    article?.title ?: "文章详情"
                ), click = {
                    if (x5WebView.canGoBack()) {
                        // 返回上个页面
                        x5WebView.goBack()
                    } else {
                        onBack.invoke()
                    }
                })
        },
        content = {
            AndroidView(
                { x5WebView }, modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 56.dp)
            ) { x5WebView ->
                x5WebView.loadUrl(article?.link)
            }
        },
        bottomBar = {
            ArticleBottomBar(
                article = article,
                coroutineScope = coroutineScope,
                collectRepository = collectRepository
            )
        }
    )
}

/**
 * 功能暂不可用的弹窗
 */
@Composable
private fun FunctionalityNotAvailablePopup(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        text = {
            Text(
                text = "Functionality not available \uD83D\uDE48",
                style = MaterialTheme.typography.body2
            )
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(text = "CLOSE")
            }
        }
    )
}

fun shareArticle(title: String, post: String, context: Context) {
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TITLE, title)
        putExtra(Intent.EXTRA_TEXT, post)
    }
    context.startActivity(
        Intent.createChooser(intent, StringUtils.getString(R.string.share_article))
    )
}
