package ac.hurley.codehub_kotlin.compose.nav

import ac.hurley.codehub_kotlin.R
import ac.hurley.codehub_kotlin.compose.MainActions
import ac.hurley.codehub_kotlin.compose.common.AppBar
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource

@Composable
fun SearchPage(onNavigationEvent: MainActions) {
    Scaffold(
        topBar = {
            AppBar(title = stringResource(id = R.string.search), click = onNavigationEvent.upPress)
        },
        content = {

        }
    )
}