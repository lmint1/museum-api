package repository

import base.Id
import entity.User

class InMemoryUserRepository(
    val memory: HashMap<Id, User> = hashMapOf()
) : UserRepository {

    override fun get(id: Id): User {
        assert(id in memory) { "User (id: $id) not found" }
        return memory[id]!!
    }

    override fun save(entity: User): User {
        return entity.also { memory[it.id] = it }
    }

    override fun update(entity: User): User {
        return entity.also { memory[it.id] = it }
    }

    override fun delete(id: Id) {
        memory.remove(id)
    }

}