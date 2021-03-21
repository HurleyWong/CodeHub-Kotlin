package ac.hurley.net.service

import ac.hurley.model.model.ArticleListModel
import ac.hurley.model.model.BaseModel
import ac.hurley.model.room.entity.Article
import ac.hurley.model.room.entity.Banner
import ac.hurley.model.room.entity.HotKey
import ac.hurley.model.room.entity.Website
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface HomeService {

    @GET("banner/json")
    suspend fun getBanner(): BaseModel<List<Banner>>

    @GET("article/top/json")
    suspend fun getTopArticles(): BaseModel<List<Article>>

    suspend fun getFriendWebsites(): BaseModel<List<Website>>

    @GET("article/list/{page}/json")
    suspend fun getArticle(@Path("page") page: Int): BaseModel<ArticleListModel>

    @GET("hotkey/json")
    suspend fun getHotKey(): BaseModel<List<HotKey>>

    @GET("article/query/{page}/json")
    suspend fun getQueryArticleList(
        @Path("page") page: Int,
        @Query("k") k: String
    ): BaseModel<ArticleListModel>
}