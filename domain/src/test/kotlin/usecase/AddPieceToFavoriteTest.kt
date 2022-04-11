package usecase

import dto.PieceItem
import entity.User
import gateway.PieceGateway
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import repository.InMemoryUserRepository
import repository.UserRepository
import kotlin.test.assertFails

class AddPieceToFavoriteTest {

    private lateinit var userRepository: UserRepository
    private lateinit var addPieceToFavorite: AddPieceToFavorite
    private lateinit var gateway: PieceGateway
    private lateinit var mock: PieceItem

    @Before
    fun setup() {
        mock = mockk(relaxed = true)
        every { mock.id }.returns(1000)
        userRepository = InMemoryUserRepository()
        gateway = mockk()
        addPieceToFavorite = AddPieceToFavorite(userRepository, gateway)
    }

    @Test fun `add piece to favorite`() {
        // Given
        val user = User("Chris", "aaaa@gmail.com")
            .also(userRepository::save)

        every { gateway.get(1000) }.answers { mock }

        // When
        addPieceToFavorite(user.id, mock.id)

        // Then
        assertTrue(userRepository.get(user.id).hasPiece(mock.id))
    }

    @Test fun `add piece to favorite twice`() {
        // Given
        val user = User("name", "email").also(userRepository::save)
        every { gateway.get(1000) }.answers { mock }

        // When
        addPieceToFavorite(user.id, mock.id)

        // Then
        assertFails { addPieceToFavorite(user.id, mock.id) }
    }



}