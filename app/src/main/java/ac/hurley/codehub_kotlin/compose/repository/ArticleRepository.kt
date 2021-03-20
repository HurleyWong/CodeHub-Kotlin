package ac.hurley.codehub_kotlin.compose.repository

import ac.hurley.codehub_kotlin.compose.AppState
import ac.hurley.model.AppDatabase
import ac.hurley.model.model.ArticleListModel
import ac.hurley.model.model.BaseModel
import ac.hurley.model.room.entity.Article
import ac.hurley.model.room.entity.ProjectClassify
import ac.hurley.net.base.QueryArticle
import android.app.Application
import androidx.lifecycle.MutableLiveData

abstract class ArticleRepository(application: Application) {

    private val projectClassifyDao = AppDatabase.getDatabase(application).projectClassifyDao()
    private val articleListDao = AppDatabase.getDatabase(application).articleDao()

    /**
     * 获得项目分类
     */
    abstract suspend fun getArticleTree(): BaseModel<List<ProjectClassify>>

    abstract suspend fun getFlag(): String

    /**
     * 获取类型
     */
    abstract suspend fun getLocalType(): Int

    /**
     * 获取文章列表
     */
    abstract suspend fun getArticleList(page:Int, cid:Int): BaseModel<ArticleListModel>

    /**
     * 获得分类列表（公众号、项目）
     */
    suspend fun getTree(state: MutableLiveData<AppState>, isRefresh: Boolean) {

    }

    /**
     * 获取文章列表
     */
    suspend fun getArticleList(state: MutableLiveData<AppState>, value: MutableLiveData<ArrayList<Article>>, query: QueryArticle) {

    }
}