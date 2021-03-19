package ac.hurley.codehub_kotlin.compose.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.chrisbanes.accompanist.insets.statusBarsHeight

@Composable
fun AppBar(
    title: String,
    showBack: Boolean = true,
    click: (() -> Unit)? = null,
    showRight: Boolean = false,
    rightImg: ImageVector = Icons.Rounded.MoreVert,
    rightClick: (() -> Unit)? = null,
) {

    // 绘制 UI 界面
    Column(modifier = Modifier.background(color = MaterialTheme.colors.primary)) {
        Spacer(Modifier.statusBarsHeight())
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(43.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            // 如果有返回箭头的按钮
            if (showBack) {
                IconButton(
                    modifier = Modifier.wrapContentWidth(Alignment.Start),
                    onClick = click!!
                ) {
                    Icon(imageVector = Icons.Rounded.ArrowBack, contentDescription = "back")
                }
            } else {
                IconButton(modifier = Modifier.wrapContentWidth(Alignment.Start), onClick = {}) {

                }
            }
            // 居中的文字
            Text(
                modifier = Modifier
                    .weight(1f)
                    .wrapContentWidth(Alignment.CenterHorizontally)
                    .padding(horizontal = 60.dp),
                text = title,
                style = MaterialTheme.typography.subtitle1,
                maxLines = 1, overflow = TextOverflow.Ellipsis,
            )
            if (showRight) {
                if (rightClick != null) {
                    IconButton(
                        modifier = Modifier.wrapContentWidth(Alignment.End),
                        onClick = rightClick
                    ) {
                        Icon(imageVector = rightImg, contentDescription = "more")
                    }
                }
            } else {
                IconButton(
                    modifier = Modifier.wrapContentWidth(Alignment.Start), onClick = {}
                ) {}
            }
        }
    }
}