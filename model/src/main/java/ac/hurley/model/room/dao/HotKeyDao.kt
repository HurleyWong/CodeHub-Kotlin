package ac.hurley.model.room.dao

import ac.hurley.model.room.entity.HotKey
import androidx.room.*

@Dao
interface HotKeyDao {

    @Query("select * from hot_key order by uid desc")
    suspend fun getHotKeyList(): List<HotKey>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addHotKeyList(hotKeyList: List<HotKey>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addHotKey(hotKey: HotKey)

    @Update
    suspend fun updateHotKey(hotKey: HotKey): Int

    @Delete
    suspend fun deleteHotKey(hotKey: HotKey): Int

    @Delete
    suspend fun deleteHotKeyList(hotKeyList: List<HotKey>): Int

    @Query("delete from hot_key")
    suspend fun deleteAllHotKey()
}