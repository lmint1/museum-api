package entity

import base.Entity
import base.Id
import dto.PieceItem
import java.util.*

open class Piece(
    val title: String,
    val objectId: Long,
    val artist: String?,
    val culture: String?,
    val dated: String?,
    val imageUrl: String?,
    val classification: String,
    val userId: Id,
    override val id: Id = UUID.randomUUID(),
    likes: Long = 0,
): Entity {

    constructor(dto: PieceItem, userId: Id): this(dto, 0, UUID.randomUUID(), userId)

    constructor(dto: PieceItem, likes: Long, id: Id, userId: Id): this(
        id = id,
        userId = userId,
        likes = likes,
        title = dto.title ?: "undefined",
        objectId = dto.id,
        artist = dto.artist,
        culture = dto.culture,
        dated = dto.dated,
        imageUrl = dto.imageUrl,
        classification = dto.classification ?: "undefined"
    )

    var likes: Long = likes
        private set

    fun like() { likes++ }

}