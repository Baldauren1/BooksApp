package kz.aitu.booksapp.data.repo

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class FirebaseFavoritesRepository(
    private val db: FirebaseDatabase = FirebaseDatabase.getInstance(),
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
) {
    private fun uid(): String = auth.currentUser?.uid ?: error("Not authenticated")

    private fun favoritesRef(): DatabaseReference =
        db.getReference("users").child(uid()).child("favorites")

    suspend fun setFavorite(bookId: String, isFavorite: Boolean) {
        val ref = favoritesRef().child(bookId)
        if (isFavorite) ref.setValue(true).await() else ref.removeValue().await()
    }

    suspend fun isFavorite(bookId: String): Boolean {
        val snap = favoritesRef().child(bookId).get().await()
        return snap.exists()
    }

    fun observeFavoriteIds(): Flow<Set<String>> = callbackFlow {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val ids = snapshot.children.mapNotNull { it.key }.toSet()
                trySend(ids)
            }

            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }

        favoritesRef().addValueEventListener(listener)
        awaitClose { favoritesRef().removeEventListener(listener) }
    }
}
