package kz.aitu.booksapp.data

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await

class FirebaseAuthRepository(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
) {
    fun currentUser() = auth.currentUser

    suspend fun signIn(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password).await()
    }

    suspend fun signUp(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password).await()
    }

    fun signOut() {
        auth.signOut()
    }
}
