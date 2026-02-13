import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "books",
    indices = [Index(value = ["cachedAt"])]
)
data class BookEntity(
    @PrimaryKey val id: String,
    val title: String,
    val authors: String,
    val thumbnailUrl: String?,
    val description: String,
    val cachedAt: Long
)
