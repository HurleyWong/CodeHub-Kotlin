package ac.hurley.util.util

import android.text.Html

/**
 * 获得 HTML 中的文字
 */
fun getHtmlText(text: String): String {
    return if (VersionUtils.hasNougat()) {
        Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY).toString()
    } else {
        text
    }
}