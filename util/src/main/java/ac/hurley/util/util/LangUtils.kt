package ac.hurley.util.util

import java.util.*

fun isZhLang(): Boolean {
    return Locale.getDefault().language == "zh"
}