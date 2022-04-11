package entity

import io.mockk.mockk
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import java.util.*

class PieceTest {

    private lateinit var piece: Piece

    @Before fun setup() {
        piece = Piece(mockk(relaxed = true), UUID.randomUUID())
    }

    @Test fun `like piece test`() {
        val expected = piece.likes + 1
        piece.like()
        assertEquals(expected, piece.likes)
    }

}