package kz.aitu.booksapp.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kz.aitu.booksapp.data.FirebaseCommentsRepository
import kz.aitu.booksapp.model.Comment
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class CommentsUiState(
    val loading: Boolean = true,
    val comments: List<Comment> = emptyList(),
    val error: String? = null
)

class CommentsViewModel(
    private val repo: FirebaseCommentsRepository = FirebaseCommentsRepository()
) : ViewModel() {
    private val _state = MutableStateFlow(CommentsUiState())
    val state: StateFlow<CommentsUiState> = _state

    fun start(bookId: String) {
        viewModelScope.launch {
            _state.value = CommentsUiState(loading = true)
            try {
                repo.observeComments(bookId).collect { list ->
                    _state.value = CommentsUiState(loading = false, comments = list)
                }
            } catch (e: Exception) {
                _state.value = CommentsUiState(loading = false, error = e.message)
            }
        }
    }

    fun add(bookId: String, text: String) {
        val cleaned = text.trim()
        if (cleaned.isEmpty()) {
            _state.value = _state.value.copy(error = "Comment cannot be empty")
            return
        }
        viewModelScope.launch {
            try {
                repo.addComment(bookId, cleaned)
            } catch (e: Exception) {
                _state.value = _state.value.copy(error = e.message ?: "Failed to add comment")
            }
        }
    }

    fun delete(bookId: String, commentId: String) {
        viewModelScope.launch {
            try {
                repo.deleteComment(bookId, commentId)
            } catch (e: Exception) {
                _state.value = _state.value.copy(error = e.message ?: "Failed to delete")
            }
        }
    }

    fun clearError() {
        _state.value = _state.value.copy(error = null)
    }
}
