package ac.hurley.codehub_kotlin.compose.view

import ac.hurley.codehub_kotlin.R
import ac.hurley.codehub_kotlin.compose.common.NetworkImage
import ac.hurley.model.room.entity.Article
import ac.hurley.util.util.getHtmlText
import android.text.TextUtils
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun ArticleListItem(
    article: Article,
    index: Int,
    toArticle: (urlArgs: Article) -> Unit,
) {
    Row(modifier = Modifier.padding(top = 8.dp, start = 2.dp, end = 2.dp)) {

        ArticleItem(
            article = article,
            onClick = { toArticle(article) },
            modifier = Modifier.height(100.dp),
            shape = RoundedCornerShape(topStart = 24.dp),
//            shape = RectangleShape,
        )
    }
}

@Composable
fun ArticleItem(
    article: Article,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    shape: Shape = RectangleShape,
    elevation: Dp = 0.dp,
    titleStyle: TextStyle = MaterialTheme.typography.subtitle1
) {
    Surface(elevation = elevation, shape = shape, modifier = modifier) {
        Row(modifier = Modifier.clickable(onClick = onClick)) {
            Column(
                modifier = Modifier.padding(
                    start = 16.dp,
                    top = 8.dp,
                    end = 16.dp,
                    bottom = 8.dp
                )
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {

                    // 文章作者
                    Text(
                        text = if (TextUtils.isEmpty(article.author)) article.shareUser else article.author,
                        color = MaterialTheme.colors.primary,
                        style = MaterialTheme.typography.subtitle1,
                        modifier = Modifier
                            .weight(1f)
                            .wrapContentWidth(Alignment.Start)
                    )
                    // 文章发布时间
                    Text(
                        text = article.niceDate,
                        color = MaterialTheme.colors.primary,
                        style = MaterialTheme.typography.subtitle1,
                        modifier = Modifier
                            .weight(1f)
                            .wrapContentWidth(Alignment.End)
                    )
                }


                // 文章标题
                Text(
                    text = getHtmlText(article.title),
                    style = MaterialTheme.typography.body1,
                    maxLines = 2,
                    // 使用省略号
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .padding(top = 12.dp, bottom = 12.dp)
                )
            }
        }
    }
}
