package ac.hurley.codehub_kotlin.compose.view

import ac.hurley.codehub_kotlin.compose.common.NetworkImage
import ac.hurley.model.room.entity.Article
import ac.hurley.model.room.entity.Banner
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.blankj.utilcode.util.ImageUtils
import com.blankj.utilcode.util.LogUtils

@Composable
fun BannerCard(banner: Banner, navigateTo: (Article) -> Unit, modifier: Modifier = Modifier) {
    Card(
        shape = MaterialTheme.shapes.medium,
        modifier = modifier.size(300.dp, 180.dp)
    ) {
        Column(modifier = Modifier.clickable(onClick = {
            navigateTo(
                Article(title = banner.title, link = banner.url)
            )
        })) {
            val bannerModifier = Modifier.size(300.dp, 180.dp)

            // 如果数据库中不存在已有的轮播图路径
            if (banner.filePath == "") {
                LogUtils.d("加载网络图片")
                NetworkImage(
                    url = banner.imagePath,
                    contentDescription = null,
                    modifier = Modifier.size(300.dp, 180.dp)
                )
            } else {
                LogUtils.d("加载本地图片")
                // 加载本地图片
                val bitmap = ImageUtils.getBitmap(banner.filePath)
                Image(
                    modifier = bannerModifier,
                    painter = BitmapPainter(bitmap.asImageBitmap()),
                    contentDescription = "",
                    contentScale = ContentScale.Crop
                )
            }


//            Column(modifier = Modifier.padding(16.dp)) {
//                Text(
//                    text = banner.title,
//                    style = MaterialTheme.typography.h6,
//                    maxLines = 2,
//                    overflow = TextOverflow.Ellipsis
//                )
//                Text(
//                    text = banner.title,
//                    maxLines = 1,
//                    overflow = TextOverflow.Ellipsis,
//                    style = MaterialTheme.typography.body2
//                )
//                Text(
//                    text = "${banner.type} - ",
//                    style = MaterialTheme.typography.body2
//                )
//            }
        }
    }

}