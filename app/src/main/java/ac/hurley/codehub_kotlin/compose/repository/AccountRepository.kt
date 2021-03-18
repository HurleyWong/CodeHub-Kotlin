package ac.hurley.codehub_kotlin.compose.repository

import ac.hurley.net.base.CodeHubNetwork

class AccountRepository {

    suspend fun getLogin(username: String, password: String) =
        CodeHubNetwork.getLogin(username, password)

    suspend fun getRegister(username: String, password: String, repassword: String) =
        CodeHubNetwork.getRegister(username, password, repassword)

    suspend fun getLogout() = CodeHubNetwork.getLogout()

}