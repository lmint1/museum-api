package infrastructure.database

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

object Passwords: IntIdTable() {
    val password = text("password")
    val user = reference("user_id", Users)
}

class PasswordsDao(id: EntityID<Int>): IntEntity(id) {
    var password by Passwords.password
    var user by UsersDao referencedOn Passwords.user
    companion object : IntEntityClass<PasswordsDao>(Passwords)
}