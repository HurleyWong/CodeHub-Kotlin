package ac.hurley.codehub_kotlin.compose.viewmodel

import ac.hurley.codehub_kotlin.compose.repository.OfficialAccountRepository
import android.app.Application

class OfficialAccountViewModel(application: Application) :
    BasePageViewModel<Boolean>(application = application) {

    private val officialAccountRepository = OfficialAccountRepository(application = application)

    override suspend fun getData(page: Boolean) {
        return officialAccountRepository.getWxArticleTree(mutableLiveData, page)
    }


}