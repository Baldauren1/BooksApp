package kz.aitu.booksapp.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kz.aitu.booksapp.data.local.BookDao
import kz.aitu.booksapp.data.local.toDomain
import kz.aitu.booksapp.data.repo.FirebaseFavoritesRepository
import kz.aitu.booksapp.domain.model.Book
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class DetailsUiState(
    val loading: Boolean = true,
    val book: Book? = null,
    val isFavorite: Boolean = false,
    val error: String? = null
)

class DetailsViewModel(
    private val bookDao: BookDao,
    private val favRepo: FirebaseFavoritesRepository
) : ViewModel() {

    private val _state = MutableStateFlow(DetailsUiState())
    val state: StateFlow<DetailsUiState> = _state

    fun load(bookId: String) {
        viewModelScope.launch {
            _state.value = DetailsUiState(loading = true)
            try {
                val entity = bookDao.getById(bookId)
                val book = entity?.toDomain()

                val fav = if (book != null) favRepo.isFavorite(bookId) else false

                _state.value = DetailsUiState(
                    loading = false,
                    book = book,
                    isFavorite = fav,
                    error = if (book == null) "Book not found in cache. Try refreshing feed." else null
                )
            } catch (e: Exception) {
                _state.value = DetailsUiState(
                    loading = false,
                    error = e.message ?: "Failed to load details"
                )
            }
        }
    }

    fun toggleFavorite(bookId: String, makeFavorite: Boolean) {
        viewModelScope.launch {
            try {
                favRepo.setFavorite(bookId, makeFavorite)
                _state.value = _state.value.copy(isFavorite = makeFavorite, error = null)
            } catch (e: Exception) {
                _state.value = _state.value.copy(error = e.message ?: "Failed to update favorite")
            }
        }
    }

    fun clearError() {
        _state.value = _state.value.copy(error = null)
    }
}
