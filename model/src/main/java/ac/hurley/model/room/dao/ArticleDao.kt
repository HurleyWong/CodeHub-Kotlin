package ac.hurley.model.room.dao

import ac.hurley.model.room.entity.Article
import androidx.room.*

@Dao
interface ArticleDao {

    @Query("select * from article")
    suspend fun getAllArticles(): List<Article>

    @Query("select * from article where id = :id and local_type = :type")
    suspend fun getArticle(id: Int, type: Int): Article?

    @Query("select * from article where local_type = :type order by uid desc limit :page, 20")
    suspend fun getHistoryArticleList(page: Int, type: Int): List<Article>


    @Query("select * from article where local_type = :type")
    suspend fun getArticleList(type: Int): List<Article>

    @Query("select * from article where local_type = :type")
    suspend fun getTopArticleList(type: Int): List<Article>

    @Query("select * from article where local_type = :type and chapter_id = :chapterId")
    suspend fun getArticleListForChapterId(type: Int, chapterId: Int): List<Article>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addArticleList(articleList: List<Article>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addArticle(article: Article)

    @Update
    suspend fun updateArticle(article: Article): Int

    @Delete
    suspend fun deleteArticle(article: Article): Int

    @Delete
    suspend fun deleteArticleList(articleList: List<Article>): Int

    @Query("delete from article")
    suspend fun deleteAllArticles()

    @Query("delete from article where local_type = :type")
    suspend fun deleteAllArticles(type: Int)

    @Query("delete from article where local_type = :type and chapter_id = :chapterId")
    suspend fun deleteAllArticles(type: Int, chapterId: Int)
}