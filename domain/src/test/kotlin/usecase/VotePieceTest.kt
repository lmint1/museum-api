package usecase

import dto.PieceItem
import entity.Piece
import entity.User
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import repository.PieceRepository
import repository.InMemoryPieceRepository
import repository.InMemoryUserRepository
import repository.UserRepository
import java.util.*

class VotePieceTest {

    private lateinit var userRepository: UserRepository
    private lateinit var pieceRepository: PieceRepository
    private lateinit var votePiece: VotePiece
    private lateinit var mock: PieceItem

    @Before
    fun setup() {
        mock = mockk(relaxed = true)
        every { mock.id }.returns(1000)
        userRepository = InMemoryUserRepository()
        pieceRepository = InMemoryPieceRepository()
        votePiece = VotePiece(pieceRepository)
    }

    @Test fun `like user's piece`() {
        val userId = UUID.randomUUID()
        val piece = Piece(mock, userId).also(pieceRepository::save)
        val user = User("user", "email", hashMapOf(piece.objectId to piece), id = userId)
        userRepository.save(user)
        val expectedLikes = piece.likes + 1
        votePiece(piece.id)
        assertEquals(expectedLikes, pieceRepository.get(piece.id).likes)
    }

}