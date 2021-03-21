package ac.hurley.net.service

import ac.hurley.model.model.ArticleListModel
import ac.hurley.model.model.BaseModel
import ac.hurley.model.room.entity.Classify
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ProjectService {

    /**
     * 项目分类
     */
    @GET("project/tree/json")
    suspend fun getProjectTree(): BaseModel<List<Classify>>

    /**
     * 某一个分类下项目列表数据
     */
    @GET("project/list/{page}/json")
    suspend fun getProject(@Path("page") page: Int, @Query("cid") cid: Int): BaseModel<ArticleListModel>
}