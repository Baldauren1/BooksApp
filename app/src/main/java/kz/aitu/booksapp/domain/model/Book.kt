package kz.aitu.booksapp.domain.model

data class Book(
    val id: String,
    val title: String,
    val authors: String,
    val thumbnailUrl: String? = null,
    val description: String = ""
)
