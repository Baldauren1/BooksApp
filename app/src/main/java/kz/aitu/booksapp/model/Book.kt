package kz.aitu.booksapp.model

data class Book(
    val id: String,
    val title: String,
    val authors: String,
    val thumbnailUrl: String? = null,
    val description: String = ""
)
