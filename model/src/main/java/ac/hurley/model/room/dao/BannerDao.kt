package ac.hurley.model.room.dao

import ac.hurley.model.room.entity.Banner
import androidx.room.*

@Dao
interface BannerDao {

    @Query("select * from banner order by uid desc")
    suspend fun getBannerList(): List<Banner>

    @Query("select * from banner where id = :sid")
    fun loadBanner(sid: Int): Banner?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addBannerList(bannerList: List<Banner>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addBanner(banner: Banner)

    @Update
    suspend fun updateBanner(banner: Banner): Int

    @Delete
    suspend fun deleteBanner(banner: Banner): Int

    @Delete
    suspend fun deleteBannerList(bannerList: List<Banner>): Int

    @Query("delete from banner")
    suspend fun deleteAllBanners()
}