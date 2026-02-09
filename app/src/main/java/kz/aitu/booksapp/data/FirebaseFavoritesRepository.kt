package kz.aitu.booksapp.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await

class FirebaseFavoritesRepository(
    private val db: FirebaseDatabase = FirebaseDatabase.getInstance(),
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
) {
    private fun uid(): String = auth.currentUser?.uid ?: error("Not authenticated")

    suspend fun setFavorite(bookId: String, isFavorite: Boolean) {
        val ref = db.getReference("users").child(uid()).child("favorites").child(bookId)
        if (isFavorite) ref.setValue(true).await() else ref.removeValue().await()
    }

    suspend fun isFavorite(bookId: String): Boolean {
        val snap = db.getReference("users").child(uid()).child("favorites").child(bookId).get().await()
        return snap.exists()
    }
}
