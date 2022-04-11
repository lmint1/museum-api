package usecase

import dto.PieceItem
import entity.Piece
import entity.User
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertFalse
import org.junit.Before
import org.junit.Test
import repository.PieceRepository
import repository.InMemoryPieceRepository
import repository.InMemoryUserRepository
import repository.UserRepository
import java.util.*
import kotlin.test.assertFails

class RemovePieceFromFavoriteTest {

    private lateinit var removePieceFromFavorite: RemovePieceFromFavorite
    private lateinit var userRepository: UserRepository
    private lateinit var pieceRepository: PieceRepository
    private lateinit var mock: PieceItem

    @Before
    fun setup() {
        mock = mockk(relaxed = true)
        every { mock.id }.returns(1000)
        userRepository = InMemoryUserRepository()
        pieceRepository = InMemoryPieceRepository()
        removePieceFromFavorite = RemovePieceFromFavorite(userRepository, pieceRepository)
    }

    @Test fun `remove piece from favorites`() {
        // Given
        val userId = UUID.randomUUID()
        val piece = Piece(mock, userId).also(pieceRepository::save)
        val user = User( "name", "email", hashMapOf(piece.objectId to piece), id = userId)
            .also(userRepository::save)

        // When
        removePieceFromFavorite(user.id, piece.objectId)

        // Then
        assertFalse(userRepository.get(user.id).hasPiece(piece.objectId))
    }

    @Test fun `remove nonexistent piece`() {
        // Given
        val userId = UUID.randomUUID()
        val piece = Piece(mock, userId)
        val user = User("name", "email", hashMapOf(), id = userId).also(userRepository::save)

        // Then
        assertFails { removePieceFromFavorite(user.id, piece.objectId) }
    }

}