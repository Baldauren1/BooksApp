package kz.aitu.booksapp.data.remote

import kz.aitu.booksapp.BuildConfig
import kz.aitu.booksapp.data.remote.dto.BookItemDto
import kz.aitu.booksapp.domain.model.Book
import kotlinx.coroutines.delay
import retrofit2.HttpException
import java.io.IOException

class GoogleBooksRepository(
    private val api: GoogleBooksApi
) {
    suspend fun search(query: String, page: Int, pageSize: Int = 10): PageResult {
        val startIndex = page * pageSize
        val apiKey = BuildConfig.GBOOKS_KEY.takeIf { it.isNotBlank() }

        val response = retryRequest {
            api.searchBooks(
                query = query,
                startIndex = startIndex,
                maxResults = pageSize,
                apiKey = apiKey
            )
        }

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

private suspend fun <T> retryRequest(
    maxAttempts: Int = 3,
    initialDelayMs: Long = 300,
    block: suspend () -> T
): T {
    var attempt = 0
    var delayMs = initialDelayMs
    while (true) {
        try {
            return block()
        } catch (e: HttpException) {
            if (e.code() == 429) {
                throw IllegalStateException(
                    "HTTP 429: Too many requests / quota exceeded. " +
                            "Wait a bit and try again, or set GBOOKS_KEY."
                )
            }
            if (e.code() in 500..599 && attempt < maxAttempts - 1) {
                attempt++
                delay(delayMs)
                delayMs *= 3
                continue
            }
            throw e
        } catch (e: IOException) {
            if (attempt < maxAttempts - 1) {
                attempt++
                delay(delayMs)
                delayMs *= 3
                continue
            }
            throw e
        }
    }
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
