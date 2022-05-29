package dev.honegger.jasstracker.domain.services

import dev.honegger.jasstracker.domain.GuestPlayer
import dev.honegger.jasstracker.domain.Player
import dev.honegger.jasstracker.domain.PlayerSession
import dev.honegger.jasstracker.domain.RegisteredPlayer
import dev.honegger.jasstracker.domain.repositories.PlayerRepository
import io.mockk.*
import java.util.*
import kotlin.test.*

class PlayerServiceImplTest {
    private val playerRepository = mockk<PlayerRepository>()
    private val passwordHashService = mockk<PasswordHashService>()
    private val authTokenService = mockk<AuthTokenService>()
    private val player = slot<Player>()
    private val dummyToken = AuthToken("dummyToken")
    private val service = PlayerServiceImpl(playerRepository, passwordHashService, authTokenService)

    @BeforeTest
    fun setup() {
        clearMocks(playerRepository, passwordHashService, authTokenService)
        player.clear()
    }

    @AfterTest
    fun teardown() {
        confirmVerified(playerRepository, passwordHashService, authTokenService)
    }

    @Test
    fun `authenticatePlayer with correct password works`() {
        val displayName = "George"
        val username = "george"
        val password = "MyNameIsGeorge"
        val hashedPassword = "MyNameIsGeorge_hash"

        every { playerRepository.findPlayerByUsername(username) } returns RegisteredPlayer(
            id = UUID.randomUUID(),
            username = username,
            displayName = displayName,
            password = hashedPassword
        )
        every { passwordHashService.verifyPassword(hashedPassword, password) } returns true
        every { authTokenService.createToken(capture(player)) } returns dummyToken

        val token = service.authenticatePlayer(
            username,
            password,
        )

        assertEquals(dummyToken, token)
        val registeredPlayer = player.captured
        assertIs<RegisteredPlayer>(registeredPlayer)
        assertEquals(displayName, registeredPlayer.displayName)
        assertEquals(username, registeredPlayer.username)
        assertEquals(hashedPassword, registeredPlayer.password)
        verify(exactly = 1) {
            playerRepository.findPlayerByUsername(username)
            passwordHashService.verifyPassword(hashedPassword, password)
            authTokenService.createToken(registeredPlayer)
        }
    }

    @Test
    fun `authenticatePlayer with wrong password returns null`() {
        val displayName = "George"
        val username = "george"
        val password = "MyNameIsGeorge"
        val hashedPassword = "MyNameIsGeorge_hash"

        every { playerRepository.findPlayerByUsername(username) } returns RegisteredPlayer(
            id = UUID.randomUUID(),
            username = username,
            displayName = displayName,
            password = hashedPassword
        )
        every { passwordHashService.verifyPassword(hashedPassword, password) } returns false

        val token = service.authenticatePlayer(
            username,
            password,
        )

        assertNull(token)
        verify(exactly = 1) {
            playerRepository.findPlayerByUsername(username)
            passwordHashService.verifyPassword(hashedPassword, password)
        }
    }

    @Test
    fun `authenticatePlayer with wrong username returns null`() {
        val username = "george"
        val password = "MyNameIsGeorge"

        every { playerRepository.findPlayerByUsername(username) } returns null

        val token = service.authenticatePlayer(
            username,
            password,
        )

        assertNull(token)
        verify(exactly = 1) {
            playerRepository.findPlayerByUsername(username)
        }
    }

    @Test
    fun `registerPlayer registers a new RegisteredPlayer`() {
        val displayName = "George"
        val username = "george"
        val password = "MyNameIsGeorge"
        val hashedPassword = "MyNameIsGeorge_hash"

        every { passwordHashService.hashPassword(password) } returns hashedPassword
        every { playerRepository.savePlayer(any()) } just Runs
        every { authTokenService.createToken(capture(player)) } returns dummyToken

        val token = service.registerPlayer(
            displayName,
            username,
            password,
        )

        assertEquals(dummyToken, token)
        val registeredPlayer = player.captured
        assertIs<RegisteredPlayer>(registeredPlayer)
        assertEquals(displayName, registeredPlayer.displayName)
        assertEquals(username, registeredPlayer.username)
        assertEquals(hashedPassword, registeredPlayer.password)
        verify(exactly = 1) {
            passwordHashService.hashPassword(password)
            playerRepository.savePlayer(registeredPlayer)
            authTokenService.createToken(registeredPlayer)
        }
    }

    @Test
    fun `registerGuestPlayer registers a new GuestPlayer`() {
        every { playerRepository.savePlayer(any()) } just Runs
        every { authTokenService.createToken(capture(player)) } returns dummyToken

        val token = service.registerGuestPlayer()

        assertEquals(dummyToken, token)
        val guest = player.captured
        assertIs<GuestPlayer>(guest)

        verify(exactly = 1) {
            playerRepository.savePlayer(guest)
            authTokenService.createToken(guest)
        }
    }

    @Test
    fun `updatePlayerPassword with wrong oldPassword returns null`() {
        val id = UUID.randomUUID()
        val username = "dummy"
        val password = "Password"
        val wrongPassword = "Password_hash"
        val newPassword = "newPassword"
        val playerToUpdatePassword = RegisteredPlayer(id, username, "Dummy", wrongPassword)

        every { playerRepository.getPlayerOrNull(id) } returns playerToUpdatePassword
        every { passwordHashService.verifyPassword(wrongPassword, password) } returns false

        val session = PlayerSession(id, false, username, "Dummy")
        val token = service.updatePlayerPassword(session, password, newPassword)

        assertNull(token)

        verify(exactly = 1) {
            playerRepository.getPlayerOrNull(id)
            passwordHashService.verifyPassword(wrongPassword, password)
        }
    }

    @Test
    fun `deletePlayer calls update with guest player`() {
        val id = UUID.randomUUID()
        val playerToDelete = RegisteredPlayer(id, "marcel", "Marcel", "hash")

        every { playerRepository.getPlayerOrNull(id) } returns playerToDelete
        every { playerRepository.updatePlayer(capture(player)) } just Runs

        val session = PlayerSession(id, false, "marcel", "Marcel")

        service.deletePlayer(session, playerToDelete)

        assertEquals(GuestPlayer(id), player.captured)

        verify(exactly = 1) {
            playerRepository.getPlayerOrNull(id)
            playerRepository.updatePlayer(GuestPlayer(id))
        }
    }
}
