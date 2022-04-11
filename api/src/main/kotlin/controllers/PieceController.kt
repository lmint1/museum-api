package controllers

import dto.PiecesPage
import entity.Piece
import io.javalin.Javalin
import io.javalin.plugin.openapi.dsl.document
import io.javalin.plugin.openapi.dsl.documented
import models.UserPiece
import security.Role
import services.PieceService
import java.util.*

class PieceController(private val service: PieceService) {

    private val votePieceDoc = document()
        .operation { it.summary = "Add a like in a piece" }
        .pathParam<String>("uuid")
        .json<UserPiece>("200")

    val votePiece = documented(votePieceDoc) { ctx ->
        val uuid = UUID.fromString(ctx.pathParam("uuid"))
        val result = service.votePiece(uuid).let(::UserPiece)
        ctx.status(200).json(result)
    }

    private val listAllPiecesDoc = document()
        .operation { it.summary = "List all pieces" }
        .queryParam<Int>("page")
        .json<PiecesPage>("200")

    val listAllPieces = documented(listAllPiecesDoc) { ctx ->
        val page = ctx.queryParam("page")?.toIntOrNull() ?: 1
        val result = service.listAllPieces(page)
        ctx.status(200).json(result)
    }

}

fun Javalin.addPieceController(controller: PieceController) = apply {
    post("pieces/{uuid}/like", controller.votePiece, Role.ANYONE)
    get("pieces", controller.listAllPieces, Role.ANYONE)
}
