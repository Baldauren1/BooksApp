package kz.aitu.booksapp.data.repo

import kz.aitu.booksapp.data.local.BookDao
import kz.aitu.booksapp.data.local.BookEntity
import kz.aitu.booksapp.data.local.toEntity
import kz.aitu.booksapp.data.remote.GoogleBooksRepository
import kz.aitu.booksapp.domain.model.Book

class FeedRepository(
    private val dao: BookDao,
    private val api: GoogleBooksRepository
) {
    fun observeFeed() = dao.observeAll()

    suspend fun isCacheEmpty(): Boolean = dao.count() == 0

    suspend fun refreshFeed() {
        val now = System.currentTimeMillis()

        val pageResult: GoogleBooksRepository.PageResult =
            api.search(query = "subject:programming", page = 0, pageSize = 20)

        dao.insertAll(pageResult.items.map { book ->
            book.toEntity(cachedAt = now)
        })
    }
}


private fun BookEntity.toDomain(): Book = Book(
        id = id,
        title = title,
        authors = authors,
        thumbnailUrl = thumbnailUrl,
        description = description
    )

    private fun Book.toEntity(cachedAt: Long): BookEntity = BookEntity(
        id = id,
        title = title,
        authors = authors,
        thumbnailUrl = thumbnailUrl,
        description = description,
        cachedAt = cachedAt
    )
