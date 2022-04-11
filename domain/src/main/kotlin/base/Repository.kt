package base

interface Repository<E: Entity> {

    fun get(id: Id): E
    fun save(entity: E): E
    fun update(entity: E): E
    fun delete(id: Id)

}