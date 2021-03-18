package ac.hurley.net.base

import ac.hurley.util.util.DataStoreUtils
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ServiceManager {

    private const val BASE_URL = "https://www.wanandroid.com/"
    private const val USER_LOGIN_KEY = "user/login"
    private const val USER_REGISTER_KEY = "user/register"

    /**
     * 设置 Cookie
     */
    private const val SET_COOKIE_KEY = "Cookie"

    /**
     * Cookie
     */
    private const val COOKIE_NAME = "Cookie"

    /**
     * 连接超时
     */
    private const val CONNECT_TIMEOUT = 30L

    /**
     * 读取超时
     */
    private const val READ_TIMEOUT = 10L

    /**
     * Retrofit 搭配 OkHttp 拦截器
     */
    private fun create(): Retrofit {
        val okHttpClientBuilder = OkHttpClient().newBuilder().apply {
            connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
            readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
            // 添加 cookie 拦截器
            addInterceptor {
                val request = it.request()
                val response = it.proceed(request)
                val requestUrl = request.url().toString()
                val domain = request.url().host()

                if ((requestUrl.contains(USER_LOGIN_KEY) || requestUrl.contains(USER_REGISTER_KEY))
                    && response.headers(SET_COOKIE_KEY).isNotEmpty()
                ) {
                    val cookies = response.headers(SET_COOKIE_KEY)
                    val cookie = encodeCookie(cookies)
                    saveCookie(requestUrl, domain, cookie)
                }
                response
            }
            // 添加 domain 拦截器
            addInterceptor {
                val request = it.request()
                val builder = request.newBuilder()
                val domain = request.url().host()
                if (domain.isNotEmpty()) {
                    val strDomain: String = DataStoreUtils.readStringData(domain, "")
                    val cookie: String = if (strDomain.isNotEmpty()) strDomain else ""
                    if (cookie.isNotEmpty()) {
                        builder.addHeader(COOKIE_NAME, cookie)
                    }
                }
                it.proceed(builder.build())
            }
        }
        return RetrofitBuilder(
            url = BASE_URL,
            client = okHttpClientBuilder.build(),
            gsonFactory = GsonConverterFactory.create()
        ).retrofit
    }

    /**
     * 创建 Retrofit 服务
     */
    fun <T> create(service: Class<T>): T = create().create(service)

    private fun saveCookie(url: String?, domain: String?, cookies: String) {
        url ?: return
        DataStoreUtils.putSyncData(url, cookies)
        domain ?: return
        DataStoreUtils.putSyncData(domain, cookies)
    }
}

fun encodeCookie(cookies: List<String>): String {
    val sb = StringBuilder()
    val set = HashSet<String>()
    cookies
        .map { cookie ->
            cookie.split(";".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        }
        .forEach { it ->
            it.filterNot { set.contains(it) }.forEach { set.add(it) }
        }
    val iter = set.iterator()
    while (iter.hasNext()) {
        val cookie = iter.next()
        sb.append(cookie).append(";")
    }

    val last = sb.lastIndexOf(";")
    if (sb.length - 1 == last) {
        sb.deleteCharAt(last)
    }
    return sb.toString()
}