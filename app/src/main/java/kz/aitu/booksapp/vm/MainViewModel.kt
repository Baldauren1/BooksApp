package kz.aitu.booksapp.vm

import androidx.lifecycle.ViewModel
import kz.aitu.booksapp.data.repo.FirebaseAuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MainViewModel(
    private val authRepo: FirebaseAuthRepository = FirebaseAuthRepository()
) : ViewModel() {
    private val _isLoggedIn = MutableStateFlow(authRepo.currentUser() != null)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn

    fun refresh() {
        _isLoggedIn.value = authRepo.currentUser() != null
    }
}
