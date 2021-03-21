package ac.hurley.codehub_kotlin.compose.repository

import ac.hurley.codehub_kotlin.compose.AppState
import ac.hurley.model.model.ArticleListModel
import ac.hurley.model.model.BaseModel
import ac.hurley.model.room.entity.Article
import ac.hurley.model.room.entity.Classify
import ac.hurley.model.room.entity.OFFICIAL_ACCOUNT
import ac.hurley.net.base.CodeHubNetwork
import ac.hurley.net.base.DOWNLOAD_OFFICIAL_ACCOUNT_ARTICLE_TIME
import ac.hurley.net.base.QueryArticle
import android.app.Application
import androidx.lifecycle.MutableLiveData

class OfficialAccountRepository(application: Application) :
    ArticleRepository(application = application) {

    override suspend fun getArticleTree(): BaseModel<List<Classify>> {
        return CodeHubNetwork.getWxArticleTree()
    }

    override suspend fun getFlag(): String {
        return DOWNLOAD_OFFICIAL_ACCOUNT_ARTICLE_TIME
    }

    override suspend fun getLocalType(): Int {
        return OFFICIAL_ACCOUNT
    }

    override suspend fun getArticleList(page: Int, cid: Int): BaseModel<ArticleListModel> {
        return CodeHubNetwork.getWxArticle(page, cid)
    }

    /**
     * 获取公众号的列表
     */
    suspend fun getWxArticleTree(state: MutableLiveData<AppState>, isRefresh: Boolean) {
        super.getTree(state = state, isRefresh = isRefresh)
    }

    /**
     * 获取某个公众号的具体文章
     */
    suspend fun getWxArticle(
        state: MutableLiveData<AppState>,
        value: MutableLiveData<ArrayList<Article>>,
        query: QueryArticle
    ) {
        super.getArticleList(state, value, query)
    }
}