package ac.hurley.model.room.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import kotlinx.parcelize.Parcelize

@Parcelize
data class Article(
    @ColumnInfo(name = "desc") var desc: String = "",
    @ColumnInfo(name = "title") var title: String = "",
) : Parcelable