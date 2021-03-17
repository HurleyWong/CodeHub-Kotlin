package ac.hurley.util.util

import android.os.Build

/**
 * 判断 Android 系统版本号
 */
object VersionUtils {
    /**
     * 判断当前手机系统版本 API 是否是 16 以上。
     * @return 16 以上返回 true，否则返回 false。
     */
    fun hasJellyBean(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN
    }

    /**
     * 判断当前手机系统版本 API 是否是 17 以上。
     * @return 17 以上返回 true，否则返回 false。
     */
    fun hasJellyBeanMR1(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1
    }

    /**
     * 判断当前手机系统版本 API 是否是 18 以上。
     * @return 18 以上返回 true，否则返回 false。
     */
    fun hasJellyBeanMR2(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2
    }

    /**
     * 判断当前手机系统版本 API 是否是 19 以上。
     * @return 19 以上返回true，否则返回 false。
     */
    fun hasKitkat(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT
    }

    /**
     * 判断当前手机系统版本 API 是否是 21 以上。
     * @return 21 以上返回 true，否则返回 false。
     */
    fun hasLollipop(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP
    }

    /**
     * 判断当前手机系统版本 API 是否是 22 以上。
     * @return 22 以上返回 true，否则返回 false。
     */
    fun hasLollipopMR1(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1
    }

    /**
     * 判断当前手机系统版本 API 是否是 23 以上。
     * @return 23 以上返回 true，否则返回 false。
     */
    fun hasMarshmallow(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
    }

    /**
     * 判断当前手机系统版本 API 是否是 24 以上。
     * @return 24 以上返回 true，否则返回 false。
     */
    fun hasNougat(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.N
    }

    /**
     * 判断当前手机系统版本 API 是否是 29 以上。
     * @return 29 以上返回 true，否则返回 false。
     */
    fun hasQ(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q
    }

    /**
     * 判断当前手机系统版本 API 是否是 29 以上。
     * @return 29 以上返回 true，否则返回 false。
     */
    fun hasR(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q
    }
}