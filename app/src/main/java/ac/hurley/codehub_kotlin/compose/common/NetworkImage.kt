package ac.hurley.codehub_kotlin.compose.common

import ac.hurley.codehub_kotlin.compose.theme.compositedOnSurface
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil.ImageLoader
import coil.annotation.ExperimentalCoilApi
import coil.intercept.Interceptor
import coil.request.ImageResult
import coil.size.PixelSize
import dev.chrisbanes.accompanist.coil.CoilImage
import dev.chrisbanes.accompanist.coil.LocalImageLoader
import okhttp3.HttpUrl


@Composable
fun NetworkImage(
    url: String,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop,
    placeholderColor: Color = MaterialTheme.colors.compositedOnSurface(alpha = 0.2f)
) {
    CoilImage(
        data = url,
        modifier = modifier,
        contentDescription = contentDescription,
        contentScale = contentScale,
        loading = {
            if (placeholderColor != null) {
                Spacer(modifier = modifier
                    .fillMaxSize()
                    .background(placeholderColor))
            }
        }
    )
}

@Composable
fun ProvideImageLoader(content: @Composable () -> Unit) {
    val context = LocalContext.current
    val loader = remember(context) {
        ImageLoader.Builder(context).componentRegistry {
            add(UnsplashSizingInterceptor)
        }.build()
    }
    CompositionLocalProvider(LocalImageLoader provides loader, content = content)
}

/**
 * 一个 Coil 拦截器，它将查询参数附加到 Unsplash url 上，以请求大小合适的图像。
 */
@OptIn(ExperimentalCoilApi::class)
private object UnsplashSizingInterceptor : Interceptor {
    override suspend fun intercept(chain: Interceptor.Chain): ImageResult {
        val data = chain.request.data
        val size = chain.size
        if (data is String &&
            data.startsWith("https://images.unsplash.com/photo-") &&
            size is PixelSize &&
            size.width > 0 &&
            size.height > 0
        ) {
            val url =
                HttpUrl.parse(data)!!.newBuilder().addQueryParameter("w", size.width.toString())
                    .addQueryParameter("h", size.height.toString())
                    .build()
            val request = chain.request.newBuilder().data(url).build()
            return chain.proceed(request)
        }
        return chain.proceed(chain.request)
    }
}
