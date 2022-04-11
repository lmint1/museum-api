package infrastructure.database

import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.Column
import java.util.*

object Users: UUIDTable(name = "users", columnName = "id") {
    val name: Column<String> = text("name")
    val email: Column<String> = text("email")
}

class UsersDao(id: EntityID<UUID>): UUIDEntity(id) {
    var name by Users.name
    var email by Users.email
    companion object : UUIDEntityClass<UsersDao>(Users)
}

