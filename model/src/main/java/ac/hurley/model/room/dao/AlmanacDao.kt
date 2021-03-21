package ac.hurley.model.room.dao

import ac.hurley.model.room.entity.Almanac
import androidx.room.*

@Dao
interface AlmanacDao {

    @Query("select * from almanac order by uid desc")
    suspend fun getAlmanacList(): List<Almanac>

    @Query("select * from almanac where julian_day = :julianDay")
    suspend fun getAlmanac(julianDay: Int): Almanac?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addAlmanacList(almanacList: List<Almanac>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addAlmanac(almanac: Almanac)

    @Update
    suspend fun updateAlmanac(almanac: Almanac): Int

    @Delete
    suspend fun deleteAlmanac(almanac: Almanac): Int

    @Delete
    suspend fun deleteAlmanacList(almanacList: List<Almanac>)

    @Query("delete from almanac")
    suspend fun deleteAllAlmanac()
}