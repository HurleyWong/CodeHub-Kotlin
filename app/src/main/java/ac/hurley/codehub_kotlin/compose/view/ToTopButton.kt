package ac.hurley.codehub_kotlin.compose.view

import ac.hurley.codehub_kotlin.R
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

/**
 * 回到顶部按钮
 */
@Composable
fun ToTopButton(listState: LazyListState) {
    val coroutineScope = rememberCoroutineScope()

    Box(modifier = Modifier.fillMaxSize()) {
        Button(modifier = Modifier
            .padding(bottom = 65.dp, end = 10.dp)
            .size(50.dp)
            .clip(CircleShape)
            .align(Alignment.BottomEnd),
            onClick = {
            coroutineScope.launch {
                listState.animateScrollToItem(index = 0)
            }
        }) {
            Image(
                modifier = Modifier.size(25.dp),
                painter = painterResource(id = R.drawable.img_to_top_normal),
                contentDescription = ""
            )
        }
    }
}