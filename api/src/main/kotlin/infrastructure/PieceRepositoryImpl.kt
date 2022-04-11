package infrastructure

import base.Id
import entity.Piece
import infrastructure.database.PieceMapper
import infrastructure.database.Pieces
import infrastructure.database.PiecesDao
import org.jetbrains.exposed.sql.insertIgnore
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import repository.PieceRepository
import java.util.*

class PieceRepositoryImpl: PieceRepository {

    override fun get(id: Id): Piece = transaction {
        val find = PiecesDao.findById(id) ?: throw Exception("Cannot find piece with id: $id")
        return@transaction PieceMapper.map(find)
    }

    override fun save(entity: Piece): Piece = transaction {
        Pieces.insertIgnore {
            it[likes] = entity.likes
            it[title] = entity.title
            it[objectId] = entity.objectId
            it[artist] = entity.artist
            it[culture] = entity.culture
            it[dated] = entity.dated
            it[imageUrl] = entity.imageUrl
            it[classification] = entity.classification
            it[user] = entity.userId
        }
        return@transaction entity
    }

    override fun update(entity: Piece): Piece = transaction {
        PiecesDao.findById(entity.id)?.apply {
            likes = entity.likes
            title = entity.title
            objectId = entity.objectId
            artist = entity.artist
            culture = entity.culture
            dated = entity.dated
            imageUrl = entity.imageUrl
            classification = entity.classification
        }
        return@transaction entity
    }

    override fun delete(id: Id): Unit = transaction {
        PiecesDao.findById(id)?.delete()
    }

}