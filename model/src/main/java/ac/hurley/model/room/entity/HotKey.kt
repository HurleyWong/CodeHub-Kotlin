package ac.hurley.model.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "hot_key")
data class HotKey(
    @PrimaryKey(autoGenerate = true) val uid: Int = 0,
    @ColumnInfo(name = "id") val id: Int = 0,
    @ColumnInfo(name = "link") val link: String = "",
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "order") val order: Int = 0,
    @ColumnInfo(name = "visible") val visible: Int = 0
)