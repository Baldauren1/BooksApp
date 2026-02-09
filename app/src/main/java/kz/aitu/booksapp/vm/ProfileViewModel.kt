package kz.aitu.booksapp.vm

import androidx.lifecycle.ViewModel
import kz.aitu.booksapp.data.repo.FirebaseAuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ProfileViewModel(
    private val repo: FirebaseAuthRepository = FirebaseAuthRepository()
) : ViewModel() {
    private val _email = MutableStateFlow(repo.currentUser()?.email.orEmpty())
    val email: StateFlow<String> = _email

    fun signOut() = repo.signOut()
}
