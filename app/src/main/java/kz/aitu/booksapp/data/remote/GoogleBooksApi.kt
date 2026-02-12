package kz.aitu.booksapp.data.remote

import kz.aitu.booksapp.data.remote.dto.BooksResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface GoogleBooksApi {

    @GET("volumes")
    suspend fun searchBooks(
        @Query("q") query: String,
        @Query("startIndex") startIndex: Int,
        @Query("maxResults") maxResults: Int = 10
    ): BooksResponseDto
}
