package kz.aitu.booksapp.data.local

import BookEntity
import kz.aitu.booksapp.domain.model.Book

fun BookEntity.toDomain(): Book = Book(
    id = id,
    title = title,
    authors = authors,
    thumbnailUrl = thumbnailUrl,
    description = description
)

fun Book.toEntity(cachedAt: Long): BookEntity = BookEntity(
    id = id,
    title = title,
    authors = authors,
    thumbnailUrl = thumbnailUrl,
    description = description,
    cachedAt = cachedAt
)
