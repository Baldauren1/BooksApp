package kz.aitu.booksapp.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kz.aitu.booksapp.data.remote.GoogleBooksRepository
import kz.aitu.booksapp.domain.model.Book
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class SearchUiState(
    val query: String = "",
    val items: List<Book> = emptyList(),
    val loading: Boolean = false,
    val error: String? = null,
    val page: Int = 0,
    val endReached: Boolean = false
)

class SearchViewModel(
    private val repo: GoogleBooksRepository
) : ViewModel() {

    private val _state = MutableStateFlow(SearchUiState())
    val state: StateFlow<SearchUiState> = _state

    private var debounceJob: Job? = null

    fun onQueryChange(newQuery: String) {
        _state.value = _state.value.copy(query = newQuery)

        debounceJob?.cancel()
        debounceJob = viewModelScope.launch {
            delay(500) // 0.5 s after each keystroke
            val q = _state.value.query.trim()
            if (q.length >= 2) {
                searchFirstPage()
            }
        }
    }

    fun searchFirstPage() {
        val q = _state.value.query.trim()
        if (q.isEmpty()) {
            _state.value = _state.value.copy(error = "Search query cannot be empty")
            return
        }

        viewModelScope.launch {
            _state.value = _state.value.copy(
                loading = true,
                error = null,
                items = emptyList(),
                page = 0,
                endReached = false
            )
            try {
                val result = repo.search(q, page = 0)
                _state.value = _state.value.copy(
                    items = result.items,
                    page = result.nextPage,
                    endReached = result.endReached,
                    loading = false
                )
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    loading = false,
                    error = e.message ?: "Search failed"
                )
            }
        }
    }

    fun loadMore() {
        val s = _state.value
        if (s.loading || s.endReached) return

        val q = s.query.trim()
        if (q.isEmpty()) return

        viewModelScope.launch {
            _state.value = s.copy(loading = true, error = null)
            try {
                val result = repo.search(q, page = s.page)
                _state.value = _state.value.copy(
                    items = _state.value.items + result.items,
                    page = result.nextPage,
                    endReached = result.endReached,
                    loading = false
                )
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    loading = false,
                    error = e.message ?: "Load more failed"
                )
            }
        }
    }

    fun clearError() {
        _state.value = _state.value.copy(error = null)
    }
}
