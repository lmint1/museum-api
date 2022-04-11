package dto

data class PiecesPage(
    val list: List<PieceItem>,
    val nextPage: Int
)
