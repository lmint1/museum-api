package infrastructure

import config.koinSetup
import dto.PieceItem
import entity.Piece
import entity.User
import infrastructure.database.Passwords
import infrastructure.database.Pieces
import infrastructure.database.Users
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.test.KoinTest
import org.koin.test.KoinTestRule
import org.koin.test.inject
import repository.UserRepository
import java.util.*
import kotlin.test.assertFails

class UserRepositoryImplTest: KoinTest {

    private val repository: UserRepository by inject()

    @get:Rule
    val koinTestRule = KoinTestRule.create(koinSetup)

    @Before
    fun setup() {
        transaction { SchemaUtils.create (Users, Passwords, Pieces) }
    }

    @Test
    fun test() {
        val userId = UUID.randomUUID()
        val pieces = createNewPieces(userId)
        repository.save(User(id = userId, name = "John", email = "johnjohn@gmail.com", favoritePieces = pieces))

        val user = repository.get(userId)
        assertNotNull(user)
        assertEquals(pieces.keys, user.favoritePieces.keys)

        repository.delete(userId)

        assertFails { repository.get(userId) }
    }

    @Test
    fun `insert more than one user`() {
        val id1 = UUID.randomUUID()
        val id2 = UUID.randomUUID()
        repository.save(User(
            id = id1,
            name = "John",
            email = "john@gmail.com",
            favoritePieces = createNewPieces(id1)
        ))
        repository.save(User(
            id = id2,
            name = "Carls",
            email = "carls@gmail.com",
            favoritePieces = createNewPieces(id2)
        ))

    }

    private fun createNewPieces(userId: UUID): HashMap<Long, Piece> {
        val p1 = Piece(dto1, userId)
        val p2 = Piece(dto2, userId)
        return hashMapOf(p1.objectId to p1, p2.objectId to p2)
    }

    private val dto1 = object: PieceItem {
        override val id: Long = 313270
        override val title = "Social Settlements: United States. Illinois. Chicago. \"Gads Hill Centre\": Gads Hill Centre, Chicago, Ill.: Paulina Street."
        override val artist = "Unidentified Artist"
        override val culture = "American"
        override val dated = "c. 1903"
        override val imageUrl = "https://nrs.harvard.edu/urn-3:HUAM:OCP17247_dynmc"
        override val classification = "Photographs"
    }
    private val dto2 = object: PieceItem {
        override val id: Long = 129247
        override val title = "Untitled (young woman in long black dress standing between chair and window)"
        override val artist = "Paul Gittings"
        override val culture = "American"
        override val dated = "c. 1940, printed later"
        override val imageUrl = "https://nrs.harvard.edu/urn-3:HUAM:INV217363_dynmc"
        override val classification = "Photographs"
    }
}