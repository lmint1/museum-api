package models

import entity.Piece
import entity.User
import java.util.*

data class UserResponse(
    val id: String,
    val name: String,
    val email: String,
    val favoritePieces: List<UserPiece>
) {
    constructor(user: User): this(
        user.id.toString(), user.name, user.email, user.favoritePieces
            .values
            .map(::UserPiece)
    )
}

data class UserPiece(
    val id: String,
    val title: String,
    val objectId: Long,
    val artist: String?,
    val culture: String?,
    val dated: String?,
    val imageUrl: String?,
    val classification: String,
    val likes: Long
) {
    constructor(piece: Piece) : this(
        id = piece.id.toString(),
        title = piece.title,
        objectId = piece.objectId,
        artist = piece.artist,
        culture = piece.culture,
        dated = piece.dated,
        imageUrl = piece.imageUrl,
        classification = piece.classification,
        likes = piece.likes
    )
}