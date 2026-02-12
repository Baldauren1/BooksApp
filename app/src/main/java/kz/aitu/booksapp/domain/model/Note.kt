package kz.aitu.booksapp.domain.model

data class Note(
    val bookId: String = "",
    val text: String = "",
    val updatedAt: Long = 0L
)
