package ac.hurley.net.base

import ac.hurley.net.service.LoginService

object CodeHubNetwork {

    private val loginService = ServiceManager.create(LoginService::class.java)

    suspend fun getLogin(username: String, password: String) =
        loginService.getLogin(username, password)

    suspend fun getRegister(username: String, password: String, repassword: String) =
        loginService.getRegister(username, password, repassword)

    suspend fun getLogout() = loginService.getLogout()
}