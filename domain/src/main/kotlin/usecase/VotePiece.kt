package usecase

import base.Id
import entity.Piece
import repository.PieceRepository

class VotePiece(private val repository: PieceRepository) {

    operator fun invoke(pieceId: Id): Piece {
        val piece = repository.get(pieceId).also { it.like() }
        return repository.update(piece)
    }

}