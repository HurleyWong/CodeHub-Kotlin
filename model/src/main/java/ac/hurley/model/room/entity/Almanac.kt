package ac.hurley.model.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "almanac")
data class Almanac(
    @PrimaryKey(autoGenerate = true) val uid: Int = 0,
    @ColumnInfo(name = "julian_day") val julianDay: Int = 0,
    @ColumnInfo(name = "img_uri") val imgUri: String,
)