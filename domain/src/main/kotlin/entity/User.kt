package entity

import base.Entity
import base.Id
import java.lang.Exception
import java.util.*
import kotlin.collections.HashMap

class User (
    val name: String,
    val email: String,
    val favoritePieces: HashMap<Long, Piece> = hashMapOf(),
    override val id: Id = UUID.randomUUID(),
): Entity {

    fun addNewPiece(piece: Piece) {
        if (hasPiece(piece.objectId))
            throw Exception("The piece with id: ${piece.objectId} is already in your favorites")
        favoritePieces[piece.objectId] = piece
    }

    fun removePiece(pieceId: Long) {
        if (!hasPiece(pieceId))
            throw Exception("There is no piece with id: $pieceId")
        favoritePieces.remove(pieceId)
    }

    fun hasPiece(pieceId: Long): Boolean {
        return pieceId in favoritePieces.keys
    }

}