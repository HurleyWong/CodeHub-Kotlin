package ac.hurley.util.util

import androidx.annotation.StringRes
import com.blankj.utilcode.util.ToastUtils

fun showToast(msg: String) {
    ToastUtils.showShort(msg)
}

fun showToast(@StringRes msg: Int) {
    ToastUtils.showShort(msg)
}

fun showLongToast(msg: String) {
    ToastUtils.showLong(msg)
}

fun showLongToast(@StringRes msg: Int) {
    ToastUtils.showLong(msg)
}