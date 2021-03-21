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
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.blankj.utilcode.util.LogUtils

@Composable
fun ArticleListItem(
    article: Article,
    index: Int,
    toArticle: (urlArgs: Article) -> Unit,
) {
    Row(modifier = Modifier.padding(top = 8.dp)) {
//        val stagger = if (index % 2 == 0) 42.dp else 16.dp
//        Spacer(modifier = Modifier.width(stagger))

        ArticleItem(
            article = article,
            onClick = { toArticle(article) },
            modifier = Modifier.height(96.dp),
//            shape = RoundedCornerShape(topStart = 24.dp)
            shape = RectangleShape,
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
            if (article.envelopePic.isNotBlank() && !article.envelopePic.contains("default_project_img")) {
                NetworkImage(
                    url = article.envelopePic,
                    contentDescription = null,
                    modifier = Modifier.aspectRatio(1f)
                )
            } else {
                Image(
                    painter = painterResource(id = R.drawable.img_default),
                    contentDescription = null,
                    modifier = Modifier
                        .height(96.dp)
                        .width(100.dp)
                )
            }

            Column(
                modifier = Modifier.padding(
                    start = 16.dp,
                    top = 16.dp,
                    end = 16.dp,
                    bottom = 8.dp
                )
            ) {
                Text(
                    text = getHtmlText(article.title),
                    style = titleStyle,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .weight(1f)
                        .padding(bottom = 4.dp)
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = article.superChapterName,
                        color = MaterialTheme.colors.primary,
                        style = MaterialTheme.typography.caption,
                        modifier = Modifier
                            .padding(start = 8.dp)
                            .weight(1f)
                            .wrapContentWidth(Alignment.Start)
                    )

                    Text(
                        text = if (TextUtils.isEmpty(article.author)) article.shareUser else article.author,
                        color = MaterialTheme.colors.primary,
                        style = MaterialTheme.typography.caption,
                        modifier = Modifier
                            .padding(start = 8.dp)
                            .weight(1f)
                            .wrapContentWidth(Alignment.Start)
                    )
                }
            }
        }
    }
}