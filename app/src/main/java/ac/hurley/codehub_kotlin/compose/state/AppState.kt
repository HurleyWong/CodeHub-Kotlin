package ac.hurley.codehub_kotlin.compose

sealed class AppState
object AppLoading : AppState()
data class ReqSuccess<T>(val data: T) : AppState()
data class ReqError(val e: Throwable) : AppState()