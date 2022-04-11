package services

import base.Id
import entity.User
import infrastructure.database.Passwords
import infrastructure.database.PasswordsDao
import infrastructure.database.Users
import infrastructure.database.UsersDao
import io.javalin.http.BadRequestResponse
import io.javalin.http.UnauthorizedResponse
import models.CreateUserRequest
import models.TokenResponse
import models.UserResponse
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.security.crypto.bcrypt.BCrypt
import repository.UserRepository
import usecase.AddPieceToFavorite
import usecase.RemovePieceFromFavorite
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import security.AuthManager
import security.Role
import usecase.ListUserPieces
import java.lang.Exception

class UserService(
    private val repository: UserRepository,
    private val addPiece: AddPieceToFavorite,
    private val removePiece: RemovePieceFromFavorite,
    private val authManager: AuthManager,
    val listUserPieces: ListUserPieces,
){

    fun addPieceToFavorite(userId: Id, objectId: Long): UserResponse {
        try {
            val user = addPiece(userId, objectId)
            return UserResponse(user)
        } catch (e: Exception) {
            throw BadRequestResponse(e.message ?: "")
        }
    }

    fun removePieceFromFavorite(userId: Id, objectId: Long): UserResponse {
        try {
            val user = removePiece(userId, objectId)
            return UserResponse(user)
        } catch (e: Exception) {
            throw BadRequestResponse(e.message ?: "")
        }
    }

    fun createUser(request: CreateUserRequest): UserResponse {
        val (name, email, _password) = request
        val encrypted = BCryptPasswordEncoder().encode(_password)
        return transaction {
            val newUser = repository.save(User(name, email))
            PasswordsDao.new {
                password = encrypted
                user = UsersDao.findById(newUser.id)!!
            }
            return@transaction UserResponse(newUser)
        }
    }

    fun login(email: String, password: String): TokenResponse = transaction {
        val user = UsersDao.find { Users.email eq email }.first()
        val saved = PasswordsDao.find { Passwords.user eq user.id }.first()
        assert(checkPassword(password, saved.password)) { unauthorized() }
        val token = authManager.createToken(user.id.toString(), Role.USER) ?: unauthorized()
        return@transaction TokenResponse(token)
    }

    private fun unauthorized(): Nothing {
        throw UnauthorizedResponse("Wrong email or password.")
    }

    private fun checkPassword(password: String, hashed: String): Boolean {
        return BCrypt.checkpw(password, hashed)
    }

}