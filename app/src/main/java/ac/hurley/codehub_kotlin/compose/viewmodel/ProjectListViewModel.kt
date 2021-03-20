package ac.hurley.codehub_kotlin.compose.viewmodel

import ac.hurley.codehub_kotlin.compose.repository.ProjectRepository
import ac.hurley.model.room.entity.Article
import ac.hurley.net.base.QueryArticle
import android.app.Application
import androidx.lifecycle.MutableLiveData

class ProjectListViewModel(application: Application) :
    BasePageViewModel<QueryArticle>(application = application) {

    private val projectRepository = ProjectRepository(application = application)

    private val _articleDataList = MutableLiveData<ArrayList<Article>>()

    override suspend fun getData(page: QueryArticle) {
        projectRepository.getProject(mutableLiveData, _articleDataList, page)
    }
}