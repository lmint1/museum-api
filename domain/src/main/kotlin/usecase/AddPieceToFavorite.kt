package usecase

import base.Id
import entity.Piece
import entity.User
import gateway.PieceGateway
import repository.UserRepository

class AddPieceToFavorite(
    private val userRepository: UserRepository,
    private val gateway: PieceGateway
) {

    operator fun invoke(userId: Id, objectId: Long): User {
        val user = userRepository.get(userId)
        val pieceDto = gateway.get(objectId)
        val piece = Piece(pieceDto, userId)
        user.addNewPiece(piece)
        return userRepository.save(user)
    }

}