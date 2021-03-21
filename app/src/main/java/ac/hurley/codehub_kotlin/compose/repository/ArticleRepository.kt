package ac.hurley.codehub_kotlin.compose.repository

import ac.hurley.codehub_kotlin.R
import ac.hurley.codehub_kotlin.compose.AppLoading
import ac.hurley.codehub_kotlin.compose.AppState
import ac.hurley.codehub_kotlin.compose.ReqError
import ac.hurley.codehub_kotlin.compose.ReqSuccess
import ac.hurley.model.AppDatabase
import ac.hurley.model.model.ArticleListModel
import ac.hurley.model.model.BaseModel
import ac.hurley.model.room.entity.Article
import ac.hurley.model.room.entity.Classify
import ac.hurley.model.room.entity.OFFICIAL_ACCOUNT
import ac.hurley.net.base.DOWNLOAD_OFFICIAL_ACCOUNT_ARTICLE_TIME
import ac.hurley.net.base.DOWNLOAD_PROJECT_ARTICLE_TIME
import ac.hurley.net.base.FOUR_HOUR
import ac.hurley.net.base.QueryArticle
import ac.hurley.util.util.DataStoreUtils
import ac.hurley.util.util.showToast
import android.accounts.NetworkErrorException
import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.blankj.utilcode.util.NetworkUtils
import kotlinx.coroutines.flow.first

abstract class ArticleRepository(application: Application) {

    private val classifyDao = AppDatabase.getDatabase(application).classifyDao()
    private val articleListDao = AppDatabase.getDatabase(application).articleDao()

    /**
     * 获得项目分类
     */
    abstract suspend fun getArticleTree(): BaseModel<List<Classify>>

    abstract suspend fun getFlag(): String

    /**
     * 获取类型
     */
    abstract suspend fun getLocalType(): Int

    /**
     * 获取文章列表
     */
    abstract suspend fun getArticleList(page: Int, cid: Int): BaseModel<ArticleListModel>

    /**
     * 获得分类列表（公众号、项目）
     */
    suspend fun getTree(state: MutableLiveData<AppState>, isRefresh: Boolean) {
        state.postValue(AppLoading)
        // 如果网络未连接
        if (!NetworkUtils.isConnected()) {
            showToast(R.string.bad_network)
            state.postValue(ReqError(NetworkErrorException("网络未连接")))
            return
        }
        // 如果是获取公众号数据
        val classifyLists = if (getFlag() == DOWNLOAD_OFFICIAL_ACCOUNT_ARTICLE_TIME) {
            classifyDao.getAllOfficialAccounts()
        } else {
            // 获取项目数据
            classifyDao.getAllProjects()
        }
        if (classifyLists.isNotEmpty() && !isRefresh) {
            state.postValue(ReqSuccess(classifyLists))
        } else {
            val tree = getArticleTree()
            if (tree.errorCode == 0) {
                val list = tree.data
                classifyDao.addClassifyList(list)
                state.postValue(ReqSuccess(list))
            } else {
                state.postValue(ReqError(NetworkErrorException("")))
            }
        }
    }

    /**
     * 获取文章列表
     */
    suspend fun getArticleList(
        state: MutableLiveData<AppState>,
        value: MutableLiveData<ArrayList<Article>>,
        query: QueryArticle
    ) {
        state.postValue(AppLoading)
        if (!NetworkUtils.isConnected()) {
            showToast(R.string.bad_network)
            state.postValue(ReqError(NetworkErrorException("网络未连接")))
            return
        }
        val res: java.util.ArrayList<Article>
        if (query.page == 0) {
            res = arrayListOf()
            val dataStore = DataStoreUtils
            // PROJECT 也可以
            val articleListForChapterId =
                articleListDao.getArticleListForChapterId(OFFICIAL_ACCOUNT, query.cid)
            var downloadArticleTime = 0L
            dataStore.readLongFlow(getFlag(), System.currentTimeMillis()).first {
                downloadArticleTime = it
                true
            }
            if (articleListForChapterId.isNotEmpty() && downloadArticleTime > 0 && downloadArticleTime - System.currentTimeMillis() < FOUR_HOUR && !query.isRefresh) {
                res.addAll(articleListForChapterId)
                state.postValue(ReqSuccess(res))
                value.postValue(res)
            } else {
                val tree = getArticleList(query.page, query.cid)
                if (tree.errorCode == 0) {
                    if (articleListForChapterId.isNotEmpty() && articleListForChapterId[0].link == tree.data.datas[0].link && !query.isRefresh) {
                        res.addAll(articleListForChapterId)
                        state.postValue(ReqSuccess(res))
                        value.postValue(res)
                    } else {
                        tree.data.datas.forEach {
                            it.localType = getLocalType()
                        }
                        DataStoreUtils.saveLongData(
                            getFlag(),
                            System.currentTimeMillis()
                        )
                        if (query.isRefresh) {
                            articleListDao.deleteAllArticles(getLocalType(), query.cid)
                        }
                        articleListDao.addArticleList(tree.data.datas)
                        res.addAll(tree.data.datas)
                        state.postValue(ReqSuccess(res))
                        value.postValue(res)
                    }
                } else {
                    state.postValue(ReqError(NetworkErrorException("")))
                    value.postValue(res)
                }
            }
        } else {
            res = value.value ?: arrayListOf()
            val tree = getArticleList(query.page, query.cid)
            if (tree.errorCode == 0) {
                res.addAll(tree.data.datas)
                state.postValue(ReqSuccess(res))
                value.postValue(res)
            } else {
                state.postValue(ReqError(NetworkErrorException("")))
            }
        }
    }
}