package ac.hurley.net.service

import ac.hurley.model.model.BaseModel
import ac.hurley.model.model.CollectionModel
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface CollectService {

    @GET("lg/collect/list/{page}/json")
    suspend fun getCollectionList(@Path("page") page: Int): BaseModel<CollectionModel>

    @POST("lg/collect/{id}/json")
    suspend fun collect(@Path("id") id: Int): BaseModel<Any>

    @POST("lg/uncollect_originId/{id}/json")
    suspend fun cancelCollect(@Path("id") id: Int): BaseModel<Any>
}