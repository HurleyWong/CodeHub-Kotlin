package ac.hurley.model.room.dao

import ac.hurley.model.room.entity.Classify
import androidx.room.*

@Dao
interface ClassifyDao {

    @Query("select * from classify where order_classify>144999 and order_classify<145050")
    suspend fun getAllProjects(): List<Classify>

    @Query("select * from classify where order_classify>189999 and order_classify<190020")
    suspend fun getAllOfficialAccounts(): List<Classify>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addClassifyList(classifyList: List<Classify>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addClassify(classify: Classify)

    @Delete
    suspend fun deleteClassify(classify: Classify): Int

    @Delete
    suspend fun deleteClassifyList(classifyList: List<Classify>): Int

    @Query("delete from classify")
    suspend fun deleteAllClassify()
}