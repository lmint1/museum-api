package gateway

import dto.PieceItem

interface PieceGateway {
    fun get(id: Long): PieceItem
    fun getAll(pageSize: Int, page: Int): List<PieceItem>
}