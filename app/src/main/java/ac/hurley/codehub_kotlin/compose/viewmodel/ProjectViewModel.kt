package ac.hurley.codehub_kotlin.compose.viewmodel

import ac.hurley.codehub_kotlin.compose.repository.ProjectRepository
import android.app.Application

class ProjectViewModel(application: Application) :
    BasePageViewModel<Boolean>(application = application) {

    private val projectRepository = ProjectRepository(application = application)

    override suspend fun getData(page: Boolean) {
        projectRepository.getProjectTree(mutableLiveData, page)
    }
}