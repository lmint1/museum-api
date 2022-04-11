package usecase

import dto.PiecesPage
import gateway.PieceGateway

class ListAllPieces(private val gateway: PieceGateway) {

    operator fun invoke(page: Int = 1): PiecesPage {
        val list = gateway.getAll(PAGE_SIZE, page)
        return PiecesPage(list, page + 1)
    }

    companion object {
        const val PAGE_SIZE = 5
    }

}