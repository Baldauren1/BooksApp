package kz.aitu.booksapp.data.repo

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import kz.aitu.booksapp.domain.model.Note

class FirebaseNotesRepository(
    private val db: FirebaseDatabase = FirebaseDatabase.getInstance(),
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
) {
    private fun uid(): String = auth.currentUser?.uid ?: error("Not authenticated")

    private fun noteRef(bookId: String): DatabaseReference =
        db.getReference("users").child(uid()).child("notes").child(bookId)

    fun observeNote(bookId: String): Flow<Note?> = callbackFlow {
        val ref = noteRef(bookId)
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val note = snapshot.getValue(Note::class.java)
                trySend(note)
            }
            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }
        ref.addValueEventListener(listener)
        awaitClose { ref.removeEventListener(listener) }
    }

    suspend fun upsertNote(bookId: String, text: String) {
        val cleaned = text.trim()
        val note = Note(
            bookId = bookId,
            text = cleaned,
            updatedAt = System.currentTimeMillis()
        )
        noteRef(bookId).setValue(note).await()
    }

    suspend fun deleteNote(bookId: String) {
        noteRef(bookId).removeValue().await()
    }
}
