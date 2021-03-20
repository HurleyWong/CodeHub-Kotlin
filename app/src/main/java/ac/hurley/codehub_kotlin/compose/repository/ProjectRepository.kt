package ac.hurley.codehub_kotlin.compose.repository

import ac.hurley.codehub_kotlin.compose.AppState
import ac.hurley.model.model.ArticleListModel
import ac.hurley.model.model.BaseModel
import ac.hurley.model.room.entity.Article
import ac.hurley.model.room.entity.PROJECT
import ac.hurley.model.room.entity.ProjectClassify
import ac.hurley.net.base.CodeHubNetwork
import ac.hurley.net.base.DOWNLOAD_PROJECT_ARTICLE_TIME
import ac.hurley.net.base.QueryArticle
import android.app.Application
import androidx.lifecycle.MutableLiveData

class ProjectRepository constructor(val application: Application) : ArticleRepository(application = application) {

    override suspend fun getArticleTree(): BaseModel<List<ProjectClassify>> {
        return CodeHubNetwork.getProjectTree()
    }

    override suspend fun getFlag(): String {
        return DOWNLOAD_PROJECT_ARTICLE_TIME
    }

    override suspend fun getLocalType(): Int {
        return PROJECT
    }

    override suspend fun getArticleList(page: Int, cid: Int): BaseModel<ArticleListModel> {
        return CodeHubNetwork.getProject(page = page, cid = cid)
    }

    /**
     * 获取项目分类标题列表
     */
    suspend fun getProjectTree(state: MutableLiveData<AppState>, isRefresh: Boolean) {
        super.getTree(state, isRefresh)
    }

    /**
     * 获取某个分类下的具体项目列表
     */
    suspend fun getProject(state: MutableLiveData<AppState>, value: MutableLiveData<ArrayList<Article>>, query: QueryArticle) {
        super.getArticleList(state = state, value = value, query = query)
    }
}