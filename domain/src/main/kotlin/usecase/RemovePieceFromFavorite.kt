package usecase

import base.Id
import entity.User
import repository.PieceRepository
import repository.UserRepository
import java.lang.Exception

class RemovePieceFromFavorite(
    private val userRepository: UserRepository,
    private val pieceRepository: PieceRepository
) {

    operator fun invoke(userId: Id, objectId: Long): User {
        val user = userRepository.get(userId)
        val piece = user.favoritePieces[objectId]
        user.removePiece(piece?.objectId ?: -1)
        pieceRepository.delete(piece!!.id)
        return userRepository.save(user)
    }

}