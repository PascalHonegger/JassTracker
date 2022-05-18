package dev.honegger.jasstracker.domain.services

import dev.honegger.jasstracker.domain.repositories.PlayerRepository
import io.mockk.*
import kotlin.test.*

class PlayerServiceImplTest {
    private val playerRepository = mockk<PlayerRepository>()
    private val passwordHashService = mockk<PasswordHashService>()
    private val service = PlayerServiceImpl(playerRepository, passwordHashService)

    @BeforeTest
    fun setup() {
        clearMocks(playerRepository, passwordHashService)
    }

    @AfterTest
    fun teardown() {
        confirmVerified(playerRepository, passwordHashService)
    }

    @Test
    fun `registerPlayer registers a new RegisteredPlayer`() {
        val displayName = "George"
        val username = "george"
        val password = "MyNameIsGeorge"
        val hashedPassword = "MyNameIsGeorge_hash"

        every { passwordHashService.hashPassword(password) } returns hashedPassword
        every { playerRepository.savePlayer(any()) } just Runs

        val created = service.registerPlayer(
            displayName,
            username,
            password,
        )

        assertEquals(displayName, created.displayName)
        assertEquals(username, created.username)
        assertEquals(hashedPassword, created.password)
        verify(exactly = 1) {
            passwordHashService.hashPassword(password)
            playerRepository.savePlayer(created)
        }
    }

    @Test
    fun `registerGuestPlayer registers a new GuestPlayer`(){
        every { playerRepository.savePlayer(any()) } just Runs

        val guest = service.registerGuestPlayer()

        verify (exactly = 1){
            playerRepository.savePlayer(guest)
        }
    }
}
