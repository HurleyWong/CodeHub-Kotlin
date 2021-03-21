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
//        val stagger = if (index % 2 == 0) 42.dp else 16.dp
//        Spacer(modifier = Modifier.width(stagger))

        ArticleItem(
            article = article,
            onClick = { toArticle(article) },
            modifier = Modifier.height(200.dp),
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
            if (article.envelopePic.isNotBlank()) {
                NetworkImage(
                    url = article.envelopePic,
                    contentDescription = null,
                    modifier = Modifier
                        .height(200.dp)
                        .width(110.dp)
                        .padding(start = 8.dp, end = 8.dp, top = 8.dp, bottom = 8.dp)
                )
            } else {
                Image(
                    // TODO 更换图片尺寸
                    painter = painterResource(id = R.drawable.img_default),
                    contentDescription = null,
                    modifier = Modifier
                        // 比例取决于 img_default 的长宽比
                        .height(100.dp)
                        .width(104.dp)
                )
            }

            Column(
                modifier = Modifier.padding(
                    start = 16.dp,
                    top = 8.dp,
                    end = 16.dp,
                    bottom = 8.dp
                )
            ) {
                // 文章标题
                Text(
                    text = getHtmlText(article.title),
                    style = titleStyle,
                    maxLines = 1,
                    // 使用省略号
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .padding(bottom = 12.dp)
                )

                // 文章描述
                Text(
                    text = getHtmlText(article.desc),
                    style = MaterialTheme.typography.subtitle2,
                    maxLines = 6,
                    // 使用省略号
                    overflow = TextOverflow.Ellipsis,
                    color = Color.DarkGray,
                    modifier = Modifier
                        .weight(1f)
                        .padding(bottom = 12.dp)
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    // 文章发布时间
                    Text(
                        text = article.niceDate.substring(0, 10),
                        color = MaterialTheme.colors.primary,
                        style = MaterialTheme.typography.caption,
                        modifier = Modifier
                            .weight(1f)
                            .wrapContentWidth(Alignment.Start)
                    )
                    // 文章作者
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