package kz.aitu.booksapp.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kz.aitu.booksapp.data.repo.BooksRepository
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
    private val booksRepo: BooksRepository,
    private val favRepo: FirebaseFavoritesRepository
) : ViewModel() {

    private val _state = MutableStateFlow(DetailsUiState())
    val state: StateFlow<DetailsUiState> = _state

    fun load(bookId: String) {
        viewModelScope.launch {
            _state.value = DetailsUiState(loading = true)
            try {
                val book = booksRepo.getById(bookId)
                if (book == null) {
                    _state.value = DetailsUiState(
                        loading = false,
                        book = null,
                        error = "Book not found"
                    )
                    return@launch
                }

                val isFav = try {
                    favRepo.isFavorite(bookId)
                } catch (_: Exception) {
                    false
                }

                _state.value = DetailsUiState(
                    loading = false,
                    book = book,
                    isFavorite = isFav,
                    error = null
                )
            } catch (e: Exception) {
                _state.value = DetailsUiState(
                    loading = false,
                    error = e.message ?: "Failed to load"
                )
            }
        }
    }

    fun toggleFavorite() {
        val current = _state.value
        val bookId = current.book?.id ?: return

        viewModelScope.launch {
            try {
                val newValue = !current.isFavorite
                favRepo.setFavorite(bookId, newValue)
                _state.value = _state.value.copy(isFavorite = newValue, error = null)
            } catch (e: Exception) {
                _state.value = _state.value.copy(error = e.message ?: "Failed to update favorite")
            }
        }
    }

    fun clearError() {
        _state.value = _state.value.copy(error = null)
    }
}
