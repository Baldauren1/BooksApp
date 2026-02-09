package kz.aitu.booksapp.data.repo

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import kz.aitu.booksapp.domain.model.Comment
import java.util.UUID

class FirebaseCommentsRepository(
    private val db: FirebaseDatabase = FirebaseDatabase.getInstance(),
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
) {
    private fun commentsRef(bookId: String): DatabaseReference =
        db.getReference("comments").child(bookId)

    fun observeComments(bookId: String): Flow<List<Comment>> = callbackFlow {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = snapshot.children.mapNotNull { it.getValue(Comment::class.java) }
                    .sortedBy { it.createdAt }
                trySend(list)
            }

            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }
        commentsRef(bookId).addValueEventListener(listener)
        awaitClose { commentsRef(bookId).removeEventListener(listener) }
    }

    suspend fun addComment(bookId: String, text: String) {
        val user = auth.currentUser ?: error("Not authenticated")
        val id = UUID.randomUUID().toString()

        val comment = Comment(
            id = id,
            bookId = bookId,
            userId = user.uid,
            userEmail = user.email.orEmpty(),
            text = text,
            createdAt = System.currentTimeMillis()
        )

        commentsRef(bookId).child(id).setValue(comment).await()
    }

    suspend fun deleteComment(bookId: String, commentId: String) {
        val user = auth.currentUser ?: error("Not authenticated")
        val ref = commentsRef(bookId).child(commentId)
        val snap = ref.get().await()
        val c = snap.getValue(Comment::class.java) ?: return
        if (c.userId != user.uid) error("You can delete only your own comments")
        ref.removeValue().await()
    }
}