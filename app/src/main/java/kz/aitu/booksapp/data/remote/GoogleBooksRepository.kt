package kz.aitu.booksapp.data.remote

import kz.aitu.booksapp.data.remote.dto.BookItemDto
import kz.aitu.booksapp.domain.model.Book

class GoogleBooksRepository(
    private val api: GoogleBooksApi
) {
    suspend fun search(query: String, page: Int, pageSize: Int = 10): PageResult {
        val startIndex = page * pageSize
        val response = api.searchBooks(
            query = query,
            startIndex = startIndex,
            maxResults = pageSize
        )

        val items = response.items.orEmpty().map { it.toDomain() }

        val total = response.totalItems
        val endReached = startIndex + items.size >= total

        return PageResult(
            items = items,
            nextPage = page + 1,
            endReached = endReached
        )
    }

    data class PageResult(
        val items: List<Book>,
        val nextPage: Int,
        val endReached: Boolean
    )
}

private fun BookItemDto.toDomain(): Book {
    val info = volumeInfo
    val authorsStr = info?.authors?.joinToString(", ").orEmpty()
    val thumb = info?.imageLinks?.thumbnail?.replace("http://", "https://")

    return Book(
        id = id,
        title = info?.title.orEmpty(),
        authors = authorsStr,
        thumbnailUrl = thumb,
        description = info?.description.orEmpty()
    )
}
