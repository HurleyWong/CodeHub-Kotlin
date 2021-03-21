package ac.hurley.model.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "classify")
data class Classify(
    @PrimaryKey(autoGenerate = true) val uid: Int,
    @ColumnInfo(name = "course_id") val courseId: Int,
//    @ColumnInfo(name = "children") val children: List<Any> = arrayListOf(),
    @ColumnInfo(name = "id") val id: Int,
    @ColumnInfo(name = "name") val name: String,
    // 不将列命名为 order，因为会和其它表冲突
    @ColumnInfo(name = "order_classify") val order: Int,
    @ColumnInfo(name = "parent_chapter_id") val parentChapterId: Int,
    @ColumnInfo(name = "user_control_set_top") val userControlSetTop: Boolean,
    @ColumnInfo(name = "visible") val visible: Int
)