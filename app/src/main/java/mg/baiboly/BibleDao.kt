package mg.baiboly

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface BibleDao {
    @Query("SELECT * FROM verses WHERE book = :book AND chapter = :chapter ORDER BY verse")
    fun getChapter(book: Int, chapter: Int): Flow<List<Verse>>

    @Query("SELECT COUNT(DISTINCT chapter) FROM verses WHERE book = :book")
    fun getChapterCount(book: Int): Flow<Int>

    @Query("SELECT * FROM verses WHERE text LIKE '%' || :query || '%' LIMIT 200")
    fun search(query: String): Flow<List<Verse>>
}
