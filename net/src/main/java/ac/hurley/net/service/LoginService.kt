package ac.hurley.net.service

import ac.hurley.model.model.BaseModel
import ac.hurley.model.model.LoginModel
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface LoginService {

    /**
     * 登录接口
     */
    @POST("user/login")
    suspend fun getLogin(
        @Query("username") username: String,
        @Query("password") password: String
    ): BaseModel<LoginModel>

    /**
     * 注册接口
     */
    @POST("user/register")
    suspend fun getRegister(
        @Query("username") username: String,
        @Query("password") password: String,
        @Query("repassword") repassword: String
    ): BaseModel<LoginModel>

    /**
     * 注销登录接口
     */
    @GET("user/logout/json")
    suspend fun getLogout(): BaseModel<Any>
}