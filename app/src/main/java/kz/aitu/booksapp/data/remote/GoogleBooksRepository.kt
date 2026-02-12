package kz.aitu.booksapp.data.remote

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import kz.aitu.booksapp.data.remote.dto.BookItemDto
import kz.aitu.booksapp.domain.model.Book
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit

class GoogleBooksRepository {

    private val json = Json { ignoreUnknownKeys = true }

    private val client = OkHttpClient.Builder().build()

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://www.googleapis.com/books/v1/")
        .client(client)
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .build()

    private val api = retrofit.create(GoogleBooksApi::class.java)

    suspend fun search(query: String, page: Int, pageSize: Int = 10): PageResult {
        val startIndex = page * pageSize
        val response = api.searchBooks(query = query, startIndex = startIndex, maxResults = pageSize)

        val items = response.items.orEmpty().map { it.toDomain() }

        val endReached = items.size < pageSize

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
    val thumb = info?.imageLinks?.thumbnail
        ?.replace("http://", "https://")

    return Book(
        id = id,
        title = info?.title.orEmpty(),
        authors = authorsStr,
        thumbnailUrl = thumb,
        description = info?.description.orEmpty()
    )
}
