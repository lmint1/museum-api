package controllers

import base.Id
import io.javalin.Javalin
import io.javalin.http.BadRequestResponse
import io.javalin.http.Context
import io.javalin.http.UnauthorizedResponse
import io.javalin.plugin.openapi.dsl.document
import io.javalin.plugin.openapi.dsl.documented
import models.*
import security.AuthManager
import security.Role
import security.secureOperation
import services.UserService
import java.util.*

class UserController(
    private val service: UserService,
    private val authManager: AuthManager
) {

    private val loginDoc = document()
        .operation { it.summary = "Login" }
        .body<LoginRequest>()
        .json<TokenResponse>("200")
        .json<UnauthorizedResponse>("401")

    val login = documented(loginDoc) { ctx ->
        val body = ctx.bodyAsClass<LoginRequest>()
        val (email, password) = body
        val token = service.login(email, password)
        ctx.status(200).json(token)
    }

    private val createUserDoc = document()
        .operation { it.summary = "Create new user" }
        .body<CreateUserRequest>()
        .json<UserResponse>("201")
        .json<BadRequestResponse>("400")

    val createUser = documented(createUserDoc) { ctx ->
        val body = ctx.bodyAsClass<CreateUserRequest>()
        val user = service.createUser(body)
        ctx.status(201).json(user)
    }

    private val listUserPiecesDoc = document()
        .operation { it.summary = "List user's favorite pieces" }
        .pathParam<String>("uuid")
        .jsonArray<UserPiece>("200")

    val listUserPieces = documented(listUserPiecesDoc) { ctx ->
        val uuid = UUID.fromString(ctx.pathParam("uuid"))
        val pieces = service.listUserPieces(uuid).map(::UserPiece)
        ctx.status(200).json(pieces)
    }

    private val addPieceToFavoriteDoc = document()
        .secureOperation { it.summary = "Add a piece to user's favorite" }
        .body<AddOrRemovePieceRequest>()
        .json<UserResponse>("200")

    val addPieceToFavorite = documented(addPieceToFavoriteDoc) { ctx ->
        val uuid = getUserId(ctx)
        val body = ctx.bodyAsClass<AddOrRemovePieceRequest>()
        val result = service.addPieceToFavorite(uuid, body.objectId)
        ctx.status(201).json(result)
    }

    private val removePieceFromFavoriteDoc = document()
        .secureOperation { it.summary = "Remove a piece from user's favorite" }
        .body<AddOrRemovePieceRequest>()
        .json<UserResponse>("200")

    val removePieceFromFavorite = documented(removePieceFromFavoriteDoc) { ctx ->
        val uuid = getUserId(ctx)
        val body = ctx.bodyAsClass<AddOrRemovePieceRequest>()
        val result = service.removePieceFromFavorite(uuid, body.objectId)
        ctx.status(200).json(result)
    }

    private fun getUserId(ctx: Context): Id {
        val decoded = authManager.decodeToken(ctx) ?: throw UnauthorizedResponse()
        return UUID.fromString(decoded.getClaim("uuid").asString())
    }

}

fun Javalin.addUserController(controller: UserController) = apply {
    post("login", controller.login, Role.ANYONE)
    post("users", controller.createUser, Role.ANYONE)
    get("users/{uuid}/pieces", controller.listUserPieces, Role.ANYONE)
    post("users/pieces", controller.addPieceToFavorite, Role.USER)
    delete("users/pieces", controller.removePieceFromFavorite, Role.USER)
}