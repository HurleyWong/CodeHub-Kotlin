package ac.hurley.codehub_kotlin.compose.repository

import ac.hurley.net.base.CodeHubNetwork

class CollectRepository {

    suspend fun getCollectList(page: Int) = CodeHubNetwork.getCollectList(page)

    suspend fun cancelCollect(id: Int) = CodeHubNetwork.cancelCollect(id)

    suspend fun collect(id: Int) = CodeHubNetwork.collect(id)
}