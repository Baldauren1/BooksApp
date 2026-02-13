package kz.aitu.booksapp.data.repo

import kz.aitu.booksapp.data.local.BookDao
import kz.aitu.booksapp.data.local.toDomain
import kz.aitu.booksapp.data.local.toEntity
import kz.aitu.booksapp.data.remote.GoogleBooksRepository
import kz.aitu.booksapp.domain.model.Book
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FeedRepository(
    private val dao: BookDao,
    private val api: GoogleBooksRepository
) {
    fun observeFeed(): Flow<List<Book>> =
        dao.observeAll().map { entities ->
            entities.map { it.toDomain() }
        }

    suspend fun isCacheEmpty(): Boolean = dao.count() == 0

    suspend fun refreshFeed() {
        val now = System.currentTimeMillis()

        val pageResult = api.search(
            query = "subject:programming",
            page = 0,
            pageSize = 10
        )

        dao.insertAll(pageResult.items.map { it.toEntity(cachedAt = now) })
    }
}
