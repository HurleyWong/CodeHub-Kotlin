package ac.hurley.codehub_kotlin.compose.nav

import ac.hurley.codehub_kotlin.R
import ac.hurley.codehub_kotlin.compose.MainActions
import ac.hurley.codehub_kotlin.compose.common.AppBar
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

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