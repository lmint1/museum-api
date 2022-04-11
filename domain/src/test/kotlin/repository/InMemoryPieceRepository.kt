package repository

import base.Id
import entity.Piece

class InMemoryPieceRepository(
    val memory: HashMap<Id, Piece> = hashMapOf()
): PieceRepository {

    override fun get(id: Id): Piece {
        assert(id in memory) { "Piece (id: $id) not found" }
        return memory[id]!!
    }

    override fun save(entity: Piece): Piece {
        return entity.also { memory[it.id] = it }
    }

    override fun update(entity: Piece): Piece {
        return entity.also { memory[it.id] = it }
    }

    override fun delete(id: Id) {
        memory.remove(id)
    }

}