package ac.hurley.codehub_kotlin.compose.common

import ac.hurley.codehub_kotlin.R
import ac.hurley.codehub_kotlin.compose.view.X5WebView
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver

@Composable
fun rememberX5WebViewWithLifecycle(): X5WebView {
    val context = LocalContext.current
    val x5WebView = remember {
        X5WebView(context).apply {
            id = R.id.web_view
        }
    }

    val lifecycleObserver = rememberX5WebViewLifecycleObserver(x5WebView)
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    DisposableEffect(lifecycle) {
        lifecycle.addObserver(lifecycleObserver)
        onDispose {
            lifecycle.removeObserver(lifecycleObserver)
        }
    }
    return x5WebView
}

@Composable
private fun rememberX5WebViewLifecycleObserver(x5WebView: X5WebView): LifecycleEventObserver =
    remember(x5WebView) {
        LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_CREATE -> x5WebView.onCreate()
                Lifecycle.Event.ON_START -> x5WebView.onStart()
                Lifecycle.Event.ON_RESUME -> x5WebView.onResume()
                Lifecycle.Event.ON_PAUSE -> x5WebView.onPause()
                Lifecycle.Event.ON_STOP -> x5WebView.onStop()
                Lifecycle.Event.ON_DESTROY -> x5WebView.destroy()
                else -> throw IllegalStateException()
            }
        }
    }