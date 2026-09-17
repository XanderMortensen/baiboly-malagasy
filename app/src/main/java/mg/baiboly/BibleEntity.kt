package mg.baiboly

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "verses")
data class Verse(
    @PrimaryKey val id: Int,
    val book: Int,
    val chapter: Int,
    val verse: Int,
    val text: String
)
