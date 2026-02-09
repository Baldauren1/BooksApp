package kz.aitu.booksapp.data.repo.remote

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kz.aitu.booksapp.data.remote.GoogleBooksApi
import kz.aitu.booksapp.domain.model.Book
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

class GoogleBooksRepository(
    private val api: GoogleBooksApi = createApi()
) {
    suspend fun search(query: String, page: Int, pageSize: Int): List<Book> {
        val start = page * pageSize
        val resp = api.searchBooks(
            query = query,
            startIndex = start,
            maxResults = pageSize
        )

        val items = resp.items.orEmpty()

        return items.map { item ->
            val v = item.volumeInfo
            val authors = v?.authors?.joinToString(", ").orEmpty()
            val thumb = v?.imageLinks?.thumbnail?.replace("http://", "https://")
            Book(
                id = item.id,
                title = v?.title ?: "Untitled",
                authors = authors.ifBlank { "Unknown author" },
                thumbnailUrl = thumb,
                description = v?.description.orEmpty()
            )
        }
    }

    companion object {
        private fun createApi(): GoogleBooksApi {
            val json = Json { ignoreUnknownKeys = true }
            val contentType = "application/json".toMediaType()

            val retrofit = Retrofit.Builder()
                .baseUrl("https://www.googleapis.com/books/v1/")
                .addConverterFactory(json.asConverterFactory(contentType))
                .build()

            return retrofit.create(GoogleBooksApi::class.java)
        }
    }
}
