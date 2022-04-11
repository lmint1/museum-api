package infrastructure

import base.Id
import entity.Piece
import entity.User
import infrastructure.database.*
import org.jetbrains.exposed.sql.insertIgnore
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import repository.UserRepository
import java.util.*

class UserRepositoryImpl: UserRepository {

    override fun get(id: Id): User = transaction {
        val find = UsersDao.findById(id) ?: throw Exception("Cannot find user with id: $id")
        val pieces = PiecesDao.find { Pieces.user eq id }
            .groupBy { it.objectId }
            .mapValues { PieceMapper.map(it.value[0]) } as HashMap<Long, Piece>
        return@transaction with(find) {
            User(name, email, pieces, id)
        }
    }

    override fun save(entity: User): User = transaction {
        Users.insertIgnore {
            it[id] = entity.id
            it[name] = entity.name
            it[email] = entity.email
        }
        entity.favoritePieces.values.forEach { piece ->
            Pieces.insertIgnore {
                it[id] = piece.id
                it[likes] = piece.likes
                it[title] = piece.title
                it[objectId] = piece.objectId
                it[artist] = piece.artist
                it[culture] = piece.culture
                it[dated] = piece.dated
                it[imageUrl] = piece.imageUrl
                it[classification] = piece.classification
                it[user] = entity.id
            }
        }
        return@transaction entity
    }

    override fun update(entity: User): User = transaction {
        Users.update {
            it[name] = entity.name
            it[email] = entity.email
        }
        return@transaction entity
    }

    override fun delete(id: Id): Unit = transaction {
        PiecesDao.find { Pieces.user eq id }.forEach(PiecesDao::delete)
        UsersDao.findById(id)?.delete()
    }

}
