package ac.hurley.codehub_kotlin.compose.page

import ac.hurley.codehub_kotlin.R
import ac.hurley.codehub_kotlin.compose.MainActions
import ac.hurley.codehub_kotlin.compose.common.AppBar
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource

@Composable
fun AboutPage(onNavigationEvent: MainActions) {
    // 绘制 UI 界面
    Scaffold(
        topBar = {
            AppBar(
                title = stringResource(id = R.string.user),
                click = onNavigationEvent.upPress
            )
        },
        content = {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

            }
        }
    )
}
