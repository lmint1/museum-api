package usecase

import base.Id
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
import kotlin.collections.HashMap

class ListUserPiecesTest {

    private lateinit var listUserPieces: ListUserPieces
    private lateinit var userRepository: UserRepository
    private lateinit var pieceRepository: PieceRepository
    private lateinit var userId: Id

    @Before
    fun setup() {
        val pieces = createPeacesHash()
        val user = User("name", "email", pieces)
        userId = user.id
        userRepository = InMemoryUserRepository(hashMapOf(user.id to user))
        pieceRepository = InMemoryPieceRepository(pieces.mapKeys { (_, value) -> value.id } as HashMap<Id, Piece>)
        listUserPieces = ListUserPieces(userRepository)
    }

    private fun createPeacesHash(): HashMap<Long, Piece> {
        val pieces = (0..10).map {
            val mock = mockk<PieceItem>(relaxed = true)
            every { mock.id }.returns(it.toLong())
            Piece(mock, UUID.randomUUID())
        }
        return pieces
            .groupBy { it.objectId }
            .mapValues { (_, values) -> values[0] } as HashMap<Long, Piece>
    }

    @Test
    fun `list user piece`() {
        val user = userRepository.get(userId)
        val expected = user.favoritePieces.values.toList()
        assertEquals(expected, listUserPieces(userId))
    }

}