package kz.aitu.booksapp.data.local

import kz.aitu.booksapp.domain.model.Book

fun BookEntity.toDomain() = Book(
    id = id,
    title = title,
    authors = authors,
    thumbnailUrl = thumbnailUrl,
    description = description
)

fun Book.toEntity(now: Long) = BookEntity(
    id = id,
    title = title,
    authors = authors,
    thumbnailUrl = thumbnailUrl,
    description = description,
    cachedAt = now
)
