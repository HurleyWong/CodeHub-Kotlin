package ac.hurley.codehub_kotlin.compose.viewmodel

import ac.hurley.codehub_kotlin.compose.repository.OfficialAccountRepository
import ac.hurley.model.room.entity.Article
import ac.hurley.net.base.QueryArticle
import android.app.Application
import androidx.lifecycle.MutableLiveData

class OfficialAccountListViewModel(application: Application) :
    BasePageViewModel<QueryArticle>(application = application) {

    private val _articleDataList = MutableLiveData<ArrayList<Article>>()

    private val officialAccountRepository = OfficialAccountRepository(application = application)

    override suspend fun getData(page: QueryArticle) {
        return officialAccountRepository.getWxArticle(mutableLiveData, _articleDataList, page)
    }
}