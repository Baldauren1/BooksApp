package kz.aitu.booksapp.data.remote

import kz.aitu.booksapp.data.remote.dto.BookItemDto
import kz.aitu.booksapp.data.remote.dto.BooksResponseDto
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Path


interface GoogleBooksApi {

    @GET("volumes")
    suspend fun searchBooks(
        @Query("q") query: String,
        @Query("startIndex") startIndex: Int,
        @Query("maxResults") maxResults: Int = 10,
        // Optional API key. If you hit HTTP 429 (quota/rate limit), set it via BuildConfig.
        @Query("key") apiKey: String? = null
    ): BooksResponseDto

    @GET("volumes/{id}")
    suspend fun getBookById(
        @Path("id") id: String,
        @Query("key") apiKey: String? = null
    ): BookItemDto
}
