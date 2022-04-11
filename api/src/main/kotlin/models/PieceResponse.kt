package models

import dto.PieceItem
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PiecesResponse(val records: List<PieceItemResponse>)

@Serializable
data class PieceItemResponse(
    override val id: Long,
    override val title: String?,
    override val culture: String?,
    override val dated: String?,
    override val classification: String?,
    private val images: List<ImageResponse>? = null,
    private val people: List<PeopleResponse>? = null,
) : PieceItem {

    override val artist get() = people?.joinToString(separator = ",") { it.name }
        ?: "Unknown"

    override val imageUrl get() = images?.firstOrNull()?.url

}

@Serializable
data class ImageResponse(
    @SerialName("baseimageurl") val url: String?
)

@Serializable
data class PeopleResponse(val name: String)