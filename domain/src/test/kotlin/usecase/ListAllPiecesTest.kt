package usecase

import dto.PieceItem
import gateway.PieceGateway
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import usecase.ListAllPieces.Companion.PAGE_SIZE

class ListAllPiecesTest {

    private lateinit var listAllPieces: ListAllPieces
    private lateinit var gateway: PieceGateway

    @Before
    fun setup() {
        gateway = mockk()
        listAllPieces = ListAllPieces(gateway)
    }

    @Test fun `list all pieces test`() {
        val page = (0..PAGE_SIZE).map { mockk<PieceItem>(relaxed = true) }
        every { gateway.getAll(PAGE_SIZE, 1) }.returns(page)
        val pieces = listAllPieces().list
        assertEquals(page, pieces)
    }

    @Test fun `list all pieces pagination test`() {
        val expected1 = (0..PAGE_SIZE).map { mockk<PieceItem>(relaxed = true) }
        val expected2 = (0..PAGE_SIZE).map { mockk<PieceItem>(relaxed = true) }

        every { gateway.getAll(PAGE_SIZE, 1) }.returns(expected1)
        every { gateway.getAll(PAGE_SIZE, 2) }.returns(expected2)

        val (pieces1, next) = listAllPieces()

        assertEquals(expected1, pieces1)
        assertEquals(next, 2)

        val pieces2 = listAllPieces(next).list
        assertEquals(expected2, pieces2)
    }

}