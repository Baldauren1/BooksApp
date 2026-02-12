package kz.aitu.booksapp.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kz.aitu.booksapp.data.repo.FirebaseNotesRepository
import kz.aitu.booksapp.domain.model.Note
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class NoteUiState(
    val loading: Boolean = true,
    val note: Note? = null,
    val text: String = "",
    val error: String? = null,
    val savedHint: Boolean = false
)

class NoteViewModel(
    private val repo: FirebaseNotesRepository
) : ViewModel() {

    private val _state = MutableStateFlow(NoteUiState())
    val state: StateFlow<NoteUiState> = _state

    fun start(bookId: String) {
        viewModelScope.launch {
            repo.observeNote(bookId).collect { note ->
                _state.value = _state.value.copy(
                    loading = false,
                    note = note,
                    text = note?.text.orEmpty(),
                    error = null,
                    savedHint = false
                )
            }
        }
    }

    fun onTextChange(newText: String) {
        _state.value = _state.value.copy(text = newText, savedHint = false)
    }

    fun save(bookId: String) {
        val text = _state.value.text.trim()

        // validation (user-friendly)
        val validationError =
            when {
                text.isEmpty() -> "Note cannot be empty"
                text.length > 500 -> "Note is too long (max 500 chars)"
                else -> null
            }

        if (validationError != null) {
            _state.value = _state.value.copy(error = validationError)
            return
        }

        viewModelScope.launch {
            try {
                repo.upsertNote(bookId, text)
                _state.value = _state.value.copy(error = null, savedHint = true)
            } catch (e: Exception) {
                _state.value = _state.value.copy(error = e.message ?: "Failed to save note")
            }
        }
    }

    fun delete(bookId: String) {
        viewModelScope.launch {
            try {
                repo.deleteNote(bookId)
                _state.value = _state.value.copy(error = null, savedHint = true)
            } catch (e: Exception) {
                _state.value = _state.value.copy(error = e.message ?: "Failed to delete note")
            }
        }
    }

    fun clearError() {
        _state.value = _state.value.copy(error = null)
    }
}
