package infrastructure.database

import entity.Piece
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.Column
import java.util.*

object Pieces: UUIDTable(name = "pieces", columnName = "id") {
    val likes: Column<Long> = long("likes")
    val title: Column<String> = text("title")
    val objectId: Column<Long> = long("object_id")
    val artist: Column<String?> = text("artist").nullable()
    val culture: Column<String?> = text("culture").nullable()
    val dated: Column<String?> = text("dated").nullable()
    val imageUrl: Column<String?> = text("image_url").nullable()
    val classification: Column<String> = text("classification")
    val user: Column<EntityID<UUID>> = reference("user_id", Users)
}

class PiecesDao(id: EntityID<UUID>): UUIDEntity(id) {
    var likes by Pieces.likes
    var title by Pieces.title
    var objectId by Pieces.objectId
    var artist by Pieces.artist
    var culture by Pieces.culture
    var dated by Pieces.dated
    var imageUrl by Pieces.imageUrl
    var classification by Pieces.classification
    var user by UsersDao referencedOn Pieces.user
    companion object : UUIDEntityClass<PiecesDao>(Pieces)
}

object PieceMapper {
    fun map(dao: PiecesDao) = with(dao) {
        Piece(
            title,
            objectId,
            artist,
            culture,
            dated,
            imageUrl,
            classification,
            user.id.value,
            id.value,
            likes
        )
    }
}