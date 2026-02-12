package kz.aitu.booksapp.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kz.aitu.booksapp.data.repo.FeedRepository
import kz.aitu.booksapp.domain.model.Book
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class FeedUiState(
    val loading: Boolean = true,
    val items: List<Book> = emptyList(),
    val error: String? = null,
    val offlineHint: Boolean = false
)

class FeedViewModel(
    private val repo: FeedRepository
) : ViewModel() {

    private val _state = MutableStateFlow(FeedUiState())
    val state: StateFlow<FeedUiState> = _state

    init {
        observeCache()
        initialLoad()
    }

    private fun observeCache() {
        viewModelScope.launch {
            repo.observeFeed().collect { list ->
                _state.value = _state.value.copy(
                    items = list,
                    loading = false
                )
            }
        }
    }

    private fun initialLoad() {
        viewModelScope.launch {
            try {
                if (repo.isCacheEmpty()) {
                    _state.value = _state.value.copy(loading = true)
                }
                repo.refreshFeed()
                _state.value = _state.value.copy(error = null, offlineHint = false)
            } catch (e: Exception) {
                // если кэш есть — просто показываем оффлайн hint
                val hasCache = _state.value.items.isNotEmpty()
                _state.value = _state.value.copy(
                    loading = false,
                    error = if (!hasCache) (e.message ?: "Failed to load feed") else null,
                    offlineHint = hasCache
                )
            }
        }
    }

    fun refresh() {
        viewModelScope.launch {
            _state.value = _state.value.copy(loading = true, error = null)
            try {
                repo.refreshFeed()
                _state.value = _state.value.copy(loading = false, offlineHint = false)
            } catch (e: Exception) {
                val hasCache = _state.value.items.isNotEmpty()
                _state.value = _state.value.copy(
                    loading = false,
                    error = if (!hasCache) (e.message ?: "Refresh failed") else null,
                    offlineHint = hasCache
                )
            }
        }
    }

    fun clearError() {
        _state.value = _state.value.copy(error = null)
    }
}
