package ac.hurley.net.service

import ac.hurley.model.model.ArticleListModel
import ac.hurley.model.model.BaseModel
import ac.hurley.model.room.entity.Classify
import retrofit2.http.GET
import retrofit2.http.Path

interface OfficialService {

    @GET("wxarticle/chapters/json")
    suspend fun getWxArticleTree(): BaseModel<List<Classify>>

    @GET("wxarticle/list/{cid}/{page}/json")
    suspend fun getWxArticle(
        @Path("page") page: Int,
        @Path("cid") cid: Int
    ): BaseModel<ArticleListModel>
}