package kz.aitu.booksapp.domain.model

data class Comment(
    val id: String = "",
    val bookId: String = "",
    val userId: String = "",
    val userEmail: String = "",
    val text: String = "",
    val createdAt: Long = 0L
)
