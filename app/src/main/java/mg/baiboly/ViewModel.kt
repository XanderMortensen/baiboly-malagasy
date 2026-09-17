package mg.baiboly

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*

data class ReaderState(
    val book: Int = 1, val chapter: Int = 1, val verses: List<Verse> = emptyList(),
    val fontSize: Float = 17f
)

class ReaderViewModel(app: Application) : AndroidViewModel(app) {
    private val dao = BibleDatabase.get(app).dao()

    private val _book = MutableStateFlow(1)
    private val _chapter = MutableStateFlow(1)
    private val _fontSize = MutableStateFlow(17f)

    val state: StateFlow<ReaderState> = combine(_book, _chapter, _fontSize) { b, c, f ->
        ReaderState(b, c, dao.getChapter(b, c).first(), f)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ReaderState())

    fun goTo(book: Int, chapter: Int) { _book.value = book; _chapter.value = chapter }
    fun nextChapter() { _chapter.value = _chapter.value + 1 }
    fun prevChapter() { _chapter.value = (_chapter.value - 1).coerceAtLeast(1) }
    fun setFontSize(v: Float) { _fontSize.value = v }
}
