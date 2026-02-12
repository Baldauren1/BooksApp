package kz.aitu.booksapp.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BooksResponseDto(
    @SerialName("items") val items: List<BookItemDto>? = null
)

@Serializable
data class BookItemDto(
    @SerialName("id") val id: String,
    @SerialName("volumeInfo") val volumeInfo: VolumeInfoDto? = null
)

@Serializable
data class VolumeInfoDto(
    @SerialName("title") val title: String? = null,
    @SerialName("authors") val authors: List<String>? = null,
    @SerialName("description") val description: String? = null,
    @SerialName("imageLinks") val imageLinks: ImageLinksDto? = null
)

@Serializable
data class ImageLinksDto(
    @SerialName("thumbnail") val thumbnail: String? = null
)
