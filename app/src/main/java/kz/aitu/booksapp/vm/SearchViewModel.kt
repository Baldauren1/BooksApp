package kz.aitu.booksapp.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kz.aitu.booksapp.data.repo.remote.GoogleBooksRepository
import kz.aitu.booksapp.domain.model.Book
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class SearchUiState(
    val query: String = "",
    val loading: Boolean = false,
    val items: List<Book> = emptyList(),
    val error: String? = null,
    val canLoadMore: Boolean = true
)

@OptIn(FlowPreview::class)
class SearchViewModel(
    private val repo: GoogleBooksRepository
) : ViewModel() {

    private val pageSize = 20
    private var page = 0

    private val queryFlow = MutableStateFlow("")
    private val _state = MutableStateFlow(SearchUiState())
    val state: StateFlow<SearchUiState> = _state

    init {
        queryFlow
            .debounce(500) // debounced search
            .distinctUntilChanged()
            .onEach { q ->
                if (q.trim().isEmpty()) {
                    _state.value = SearchUiState(query = q, items = emptyList(), canLoadMore = false)
                } else {
                    searchFirstPage(q)
                }
            }
            .launchIn(viewModelScope)
    }

    fun onQueryChange(q: String) {
        _state.value = _state.value.copy(query = q, error = null)
        queryFlow.value = q
    }

    private fun searchFirstPage(q: String) {
        viewModelScope.launch {
            page = 0
            _state.value = _state.value.copy(loading = true, error = null, items = emptyList(), canLoadMore = true)
            try {
                val items = repo.search(q.trim(), page, pageSize)
                _state.value = _state.value.copy(
                    loading = false,
                    items = items,
                    canLoadMore = items.size == pageSize
                )
            } catch (e: Exception) {
                _state.value = _state.value.copy(loading = false, error = e.message ?: "Search failed")
            }
        }
    }

    fun loadMore() {
        val q = _state.value.query.trim()
        if (q.isEmpty() || _state.value.loading || !_state.value.canLoadMore) return

        viewModelScope.launch {
            _state.value = _state.value.copy(loading = true, error = null)
            try {
                page += 1
                val next = repo.search(q, page, pageSize)
                _state.value = _state.value.copy(
                    loading = false,
                    items = _state.value.items + next,
                    canLoadMore = next.size == pageSize
                )
            } catch (e: Exception) {
                page -= 1
                _state.value = _state.value.copy(loading = false, error = e.message ?: "Load more failed")
            }
        }
    }

    fun retry() {
        val q = _state.value.query
        if (q.isNotBlank()) searchFirstPage(q)
    }
}
