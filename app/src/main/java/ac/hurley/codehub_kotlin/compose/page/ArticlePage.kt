package ac.hurley.codehub_kotlin.compose.common.article

import ac.hurley.codehub_kotlin.compose.common.rememberX5WebViewWithLifecycle
import ac.hurley.model.room.entity.Article
import ac.hurley.util.util.getHtmlText
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView

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
        }
    )
}

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