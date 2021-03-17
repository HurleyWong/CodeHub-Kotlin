package ac.hurley.codehub_kotlin.compose.nav

import ac.hurley.codehub_kotlin.compose.MainActions
import ac.hurley.codehub_kotlin.compose.viewmodel.LoginViewModel
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun LoginPage(onNavigationEvent: MainActions, viewModel: LoginViewModel = viewModel()) {
    val state by viewModel.state.observeAsState()
}