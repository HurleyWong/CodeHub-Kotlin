package ac.hurley.net.base

import ac.hurley.net.service.*
import android.app.Service

object CodeHubNetwork {

    /**
     * 主页相关操作
     */
    private val homeService = ServiceManager.create(HomeService::class.java)

    /**
     * 获取 Banner 轮播图
     */
    suspend fun getBanner() = homeService.getBanner()

    /**
     * 获取置顶文章列表
     */
    suspend fun getTopArticleList() = homeService.getTopArticles()

    /**
     * 获取首页文章列表
     */
    suspend fun getArticleList(page: Int) = homeService.getArticle(page)

    /**
     * 获取搜索的文章列表
     */
    suspend fun getQueryArticleList(page: Int, k: String) = homeService.getQueryArticleList(page, k)

    /**
     * 获取热词
     */
    suspend fun getHotKey() = homeService.getHotKey()

    /**
     * 获取常用网站 | 友链
     */
    suspend fun getWebsites() = homeService.getFriendWebsites()

    /**
     * 登录相关操作
     */
    private val loginService = ServiceManager.create(LoginService::class.java)

    // 登录
    suspend fun getLogin(username: String, password: String) =
        loginService.getLogin(username, password)

    // 注册
    suspend fun getRegister(username: String, password: String, repassword: String) =
        loginService.getRegister(username, password, repassword)

    // 注销登录
    suspend fun getLogout() = loginService.getLogout()

    /**
     * 项目相关操作
     */
    private val projectService = ServiceManager.create(ProjectService::class.java)

    // 获取项目分类
    suspend fun getProjectTree() = projectService.getProjectTree()

    // 获取某个分类下的项目数据
    suspend fun getProject(page: Int, cid: Int) = projectService.getProject(page, cid)

    /**
     * 公众号相关操作
     */
    private val officialService = ServiceManager.create(OfficialService::class.java)

    // 获取公众号列表
    suspend fun getWxArticleTree() = officialService.getWxArticleTree()

    // 获取公众号列表下的文章
    suspend fun getWxArticle(page: Int, cid: Int) = officialService.getWxArticle(page, cid)

    /**
     * 收藏相关操作
     */
    private val collectService = ServiceManager.create(CollectService::class.java)

    /**
     * 获取收藏的文章列表
     */
    suspend fun getCollectList(page: Int) = collectService.getCollectionList(page)

    /**
     * 对该文章进行收藏
     */
    suspend fun collect(id: Int) = collectService.collect(id)

    /**
     * 对该文章取消收藏
     */
    suspend fun cancelCollect(id: Int) = collectService.cancelCollect(id)
}

data class QueryHomeArticle(var page: Int, var isRefresh: Boolean)
data class QueryArticle(var page: Int, var cid: Int, var isRefresh: Boolean)

const val ONE_DAY = 1000 * 60 * 60 * 24
const val FOUR_HOUR = 1000 * 60 * 60 * 4

const val DOWNLOAD_IMAGE_TIME = "DownloadImageTime"
const val DOWNLOAD_TOP_ARTICLE_TIME = "DownloadTopArticleTime"
const val DOWNLOAD_ARTICLE_TIME = "DownloadArticleTime"
const val DOWNLOAD_PROJECT_ARTICLE_TIME = "DownloadProjectArticleTime"
const val DOWNLOAD_OFFICIAL_ACCOUNT_ARTICLE_TIME = "DownloadOfficialAccountArticleTime"