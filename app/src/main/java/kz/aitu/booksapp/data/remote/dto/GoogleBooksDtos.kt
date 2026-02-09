package kz.aitu.booksapp.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BooksResponseDto(
    val items: List<BookItemDto>? = null,
    @SerialName("totalItems") val totalItems: Int = 0
)

@Serializable
data class BookItemDto(
    val id: String,
    @SerialName("volumeInfo") val volumeInfo: VolumeInfoDto? = null
)

@Serializable
data class VolumeInfoDto(
    val title: String? = null,
    val authors: List<String>? = null,
    val description: String? = null,
    @SerialName("imageLinks") val imageLinks: ImageLinksDto? = null
)

@Serializable
data class ImageLinksDto(
    val thumbnail: String? = null
)
