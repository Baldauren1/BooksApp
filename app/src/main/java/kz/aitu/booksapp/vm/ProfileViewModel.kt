package kz.aitu.booksapp.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kz.aitu.booksapp.data.local.BookDao
import kz.aitu.booksapp.data.local.toDomain
import kz.aitu.booksapp.data.repo.FirebaseAuthRepository
import kz.aitu.booksapp.data.repo.FirebaseFavoritesRepository
import kz.aitu.booksapp.domain.model.Book
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class ProfileUiState(
    val email: String = "",
    val favorites: List<Book> = emptyList(),
    val favoriteIds: Set<String> = emptySet(),
    val error: String? = null
)

class ProfileViewModel(
    private val authRepo: FirebaseAuthRepository,
    private val favRepo: FirebaseFavoritesRepository,
    private val bookDao: BookDao
) : ViewModel() {

    private val _state = MutableStateFlow(
        ProfileUiState(email = authRepo.currentUser()?.email.orEmpty())
    )
    val state: StateFlow<ProfileUiState> = _state

    init {
        viewModelScope.launch {
            combine(
                favRepo.observeFavoriteIds(),
                bookDao.observeAll()
            ) { ids: Set<String>, cachedEntities ->
                val cachedBooks = cachedEntities.map { it.toDomain() }
                val favBooks = cachedBooks.filter { it.id in ids }
                Triple(ids, favBooks, null as String?)
            }
                .catch { e ->
                    emit(
                        Triple(
                            emptySet<String>(),
                            emptyList<Book>(),
                            (e.message ?: "Failed to load favorites")
                        )
                    )
                }
                .collect { (ids, favBooks, err) ->
                    _state.value = _state.value.copy(
                        favoriteIds = ids,
                        favorites = favBooks,
                        error = err
                    )
                }
        }
    }

    fun removeFavorite(bookId: String) {
        viewModelScope.launch {
            try {
                favRepo.setFavorite(bookId, false)
            } catch (e: Exception) {
                _state.value = _state.value.copy(error = e.message ?: "Failed to remove favorite")
            }
        }
    }

    fun signOut() = authRepo.signOut()

    fun clearError() {
        _state.value = _state.value.copy(error = null)
    }
}
