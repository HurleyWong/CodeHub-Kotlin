package ac.hurley.model.room.dao

import ac.hurley.model.room.entity.ProjectClassify
import androidx.room.*

@Dao
interface ProjectClassifyDao {

    @Query("select * from project_classify where order_classify>144999 and order_classify<145050")
    suspend fun getAllProjects(): List<ProjectClassify>

    @Query("select * from project_classify where order_classify>189999 and order_classify<190020")
    suspend fun getAllOfficialAccounts(): List<ProjectClassify>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addProjectList(projectClassifyList: List<ProjectClassify>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addProject(projectClassify: ProjectClassify)

    @Delete
    suspend fun deleteProject(projectClassify: ProjectClassify): Int

    @Delete
    suspend fun deleteProjectList(projectClassifyList: List<ProjectClassify>): Int

    @Query("delete from project_classify")
    suspend fun deleteAllProjects()
}