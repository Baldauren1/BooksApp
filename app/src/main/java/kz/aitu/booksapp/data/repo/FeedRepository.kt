package kz.aitu.booksapp.data.repo

import kz.aitu.booksapp.data.local.BookDao
import kz.aitu.booksapp.data.local.toDomain
import kz.aitu.booksapp.data.local.toEntity
import kz.aitu.booksapp.data.remote.GoogleBooksRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kz.aitu.booksapp.domain.model.Book

class FeedRepository(
    private val dao: BookDao,
    private val remote: GoogleBooksRepository
) {
    fun observeFeed(): Flow<List<Book>> =
        dao.observeFeed().map { list -> list.map { it.toDomain() } }

    suspend fun refreshFeed() {
        // "Feed" = popular/common books, make a fixed request
        val query = "android OR kotlin OR clean code"
        val result = remote.search(query = query, page = 0, pageSize = 10)

        val now = System.currentTimeMillis()
        dao.upsertAll(result.items.map { it.toEntity(now) })
    }

    suspend fun isCacheEmpty(): Boolean =
        dao.getFeedOnce().isEmpty()
}
