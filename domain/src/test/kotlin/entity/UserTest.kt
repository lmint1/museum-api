package entity

import dto.PieceItem
import io.mockk.every
import io.mockk.mockk
import org.junit.Before
import org.junit.Test
import kotlin.test.assertFails
import kotlin.test.assertFalse
import kotlin.test.assertTrue


class UserTest {

    private lateinit var user: User
    private lateinit var piece: Piece

    @Before fun setup() {
        user = User("user", "email")
        val mock = mockk<PieceItem>(relaxed = true)
        every { mock.id }.returns(1000)
        piece = Piece(mock, user.id)
    }

    @Test fun `add new Piece test`() {
        user.addNewPiece(piece)
        assertTrue(user.hasPiece(piece.objectId))
    }

    @Test fun `add same Piece twice test`() {
        user.addNewPiece(piece)
        assertFails { user.addNewPiece(piece) }
    }

    @Test fun `remove Piece test`() {
        user.addNewPiece(piece)
        user.removePiece(piece.objectId)
        assertFalse(user.hasPiece(piece.objectId))
    }

    @Test fun `remove nonexistent Piece test`() {
        assertFails { user.removePiece(piece.objectId) }
    }

}