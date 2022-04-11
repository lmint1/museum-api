package usecase

import base.Id
import entity.Piece
import repository.UserRepository

class ListUserPieces(private val userRepository: UserRepository) {

    operator fun invoke(userId: Id): List<Piece> {
        val user = userRepository.get(userId)
        return user.favoritePieces.values.toList()
    }

}