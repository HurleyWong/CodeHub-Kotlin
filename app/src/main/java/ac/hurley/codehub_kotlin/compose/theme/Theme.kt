package ac.hurley.codehub_kotlin.compose.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

/**
 * 第一套主题
 */
@Composable
fun Theme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = if (darkTheme) {
        ThemeDark
    } else {
        ThemeLight
    }
    MaterialTheme(colors = colors, typography = typography, content = content)
}

/**
 * 第二套主题
 */
@Composable
fun Theme2(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable() () -> Unit) {
    val colors = if (darkTheme) {
        ThemeDark2
    } else {
        ThemeLight2
    }
    MaterialTheme(colors = colors, typography = typography, content = content)
}

/**
 * 亮色主题一
 */
private val ThemeLight = lightColors(
    primary = blue,
    onPrimary = Color.White,
    primaryVariant = blue,
    secondary = blue
)

/**
 * 暗色主题一
 */
private val ThemeDark = darkColors(
    primary = blueDark,
    onPrimary = Color.White,
    secondary = blueDark,
    surface = blueDark
)

/**
 * 亮色主题二
 */
private val ThemeLight2 = lightColors(
    primary = Purple300,
    onPrimary = Color.White,
    primaryVariant = Purple300,
    secondary = Purple300,
    secondaryVariant = Color(0xFF018786),
    background = Purple300,
    surface = Purple300,
    error = Color(0xFFB00020),
    onSecondary = Purple300,
    onBackground = Purple300,
    onSurface = Purple300,
    onError = Color.White
)

/**
 * 暗色主题二
 */
private val ThemeDark2 = darkColors(
    primary = Purple700,
    onPrimary = Color.White,
    primaryVariant = Purple700,
    secondary = Purple700,
    secondaryVariant = Purple700,
    background = Purple700,
    surface = Purple700,
    error = Color(0xFFB00020),
    onSecondary = Purple700,
    onBackground = Purple700,
    onSurface = Purple700,
    onError = Color.White
)
