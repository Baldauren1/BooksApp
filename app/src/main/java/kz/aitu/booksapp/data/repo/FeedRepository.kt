package kz.aitu.booksapp.data.repo

import kz.aitu.booksapp.data.local.BookDao
import kz.aitu.booksapp.data.local.toDomain
import kz.aitu.booksapp.data.local.toEntity
import kz.aitu.booksapp.data.remote.GoogleBooksRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FeedRepository(
    private val dao: BookDao,
    private val remote: GoogleBooksRepository
) {
    fun observeFeed(): Flow<List<kz.aitu.booksapp.domain.model.Book>> =
        dao.observeAll().map { list -> list.map { it.toDomain() } }

    suspend fun refreshFirstPage(defaultQuery: String = "android") {
        val page = remote.search(defaultQuery, page = 0, pageSize = 10)
        val now = System.currentTimeMillis()
        dao.upsertAll(page.items.map { it.toEntity(now) })
    }

    suspend fun loadMore(defaultQuery: String = "android", page: Int): GoogleBooksRepository.PageResult {
        val result = remote.search(defaultQuery, page = page, pageSize = 10)
        val now = System.currentTimeMillis()
        dao.upsertAll(result.items.map { it.toEntity(now) })
        return result
    }
}
