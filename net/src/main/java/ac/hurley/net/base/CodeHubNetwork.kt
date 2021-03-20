package ac.hurley.net.base

import ac.hurley.net.service.LoginService
import ac.hurley.net.service.ProjectService

object CodeHubNetwork {

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
}

data class QueryHomeArticle(var page: Int, var isRefresh: Boolean)
data class QueryArticle(var page: Int, var cid: Int, var isRefresh: Boolean)

const val DOWNLOAD_TOP_ARTICLE_TIME = "DownloadTopArticleTime"
const val DOWNLOAD_ARTICLE_TIME = "DownloadArticleTime"
const val DOWNLOAD_PROJECT_ARTICLE_TIME = "DownloadProjectArticleTime"