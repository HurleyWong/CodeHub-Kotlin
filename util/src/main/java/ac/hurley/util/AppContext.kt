package ac.hurley.util

import ac.hurley.util.util.DataStoreUtils
import android.annotation.SuppressLint
import android.content.Context

@SuppressLint("StaticFieldLeak")
object AppContext {
    private const val USERNAME = "username"
    private const val NICK_NAME = "nickname"
    private const val IS_LOGIN = "isLogin"
    private lateinit var dataStore: DataStoreUtils

    var context: Context? = null
        private set

    /**
     * 初始化接口，用于应用程序的初始化操作，在代码执行的最开始要调用
     */
    fun initialize(c: Context?) {
        context = c
        DataStoreUtils.init(context!!)
        dataStore = DataStoreUtils
    }

    val username: String
        get() = dataStore.readStringData(USERNAME)
    val nickname: String
        get() = dataStore.readStringData(NICK_NAME)

    /**
     * 根据 dataStore 中存储的信息判断用户是否已登录
     */
    var isLogin: Boolean
        get() = dataStore.readBooleanData(IS_LOGIN)
        set(bool) {
            dataStore.saveSyncBooleanData(IS_LOGIN, bool)
        }

    /**
     * 设置用户信息
     */
    fun setUserInfo(nickname: String, username: String) {
        dataStore.saveSyncStringData(NICK_NAME, nickname)
        dataStore.saveSyncStringData(USERNAME, username)
    }

    /**
     * 注销用户登录
     */
    fun logout() {
        dataStore.clearSync()
    }
}