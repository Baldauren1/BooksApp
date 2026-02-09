package kz.aitu.booksapp.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kz.aitu.booksapp.data.repo.FirebaseAuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class AuthUiState(
    val loading: Boolean = false,
    val error: String? = null
)

class AuthViewModel(
    private val repo: FirebaseAuthRepository = FirebaseAuthRepository()
) : ViewModel() {
    private val _state = MutableStateFlow(AuthUiState())
    val state: StateFlow<AuthUiState> = _state

    fun signIn(email: String, password: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _state.value = AuthUiState(loading = true)
            try {
                repo.signIn(email.trim(), password)
                _state.value = AuthUiState()
                onSuccess()
            } catch (e: Exception) {
                _state.value = AuthUiState(error = e.message ?: "Login failed")
            }
        }
    }

    fun signUp(email: String, password: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _state.value = AuthUiState(loading = true)
            try {
                repo.signUp(email.trim(), password)
                _state.value = AuthUiState()
                onSuccess()
            } catch (e: Exception) {
                _state.value = AuthUiState(error = e.message ?: "Registration failed")
            }
        }
    }
}
