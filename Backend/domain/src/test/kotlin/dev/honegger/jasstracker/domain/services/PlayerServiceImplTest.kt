package dev.honegger.jasstracker.domain.services

import dev.honegger.jasstracker.domain.GuestPlayer
import dev.honegger.jasstracker.domain.Player
import dev.honegger.jasstracker.domain.PlayerSession
import dev.honegger.jasstracker.domain.RegisteredPlayer
import dev.honegger.jasstracker.domain.exceptions.NotFoundException
import dev.honegger.jasstracker.domain.repositories.PlayerRepository
import io.mockk.*
import org.junit.jupiter.api.assertThrows
import java.util.*
import kotlin.test.*

class PlayerServiceImplTest {
    private val playerRepository = mockk<PlayerRepository>()
    private val passwordHashService = mockk<PasswordHashService>()
    private val authTokenService = mockk<AuthTokenService>()
    private val player = slot<Player>()
    private val dummyToken = AuthToken("dummyToken")
    private val service = PlayerServiceImpl(playerRepository, passwordHashService, authTokenService)
    private val dummySession = PlayerSession(UUID.randomUUID(), false, "dummy", "dummy")

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
    fun `getPlayer returns player from repository`() {
        val playerId = UUID.randomUUID()
        every { playerRepository.getPlayerOrNull(playerId) } returns GuestPlayer(playerId)
        val player = service.getPlayer(dummySession, playerId)
        assertEquals(GuestPlayer(playerId), player)
        verify(exactly = 1) {
            playerRepository.getPlayerOrNull(playerId)
        }
    }

    @Test
    fun `getPlayer throws NotFoundException if not found`() {
        val playerId = UUID.randomUUID()
        every { playerRepository.getPlayerOrNull(playerId) } returns null
        assertThrows<NotFoundException> {
            service.getPlayer(dummySession, playerId)
        }
        verify(exactly = 1) {
            playerRepository.getPlayerOrNull(playerId)
        }
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
    fun `authenticatePlayer with wrong password throws IllegalArgumentException`() {
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

        assertThrows<IllegalArgumentException> {
            service.authenticatePlayer(
                username,
                password,
            )
        }

        verify(exactly = 1) {
            playerRepository.findPlayerByUsername(username)
            passwordHashService.verifyPassword(hashedPassword, password)
        }
    }

    @Test
    fun `authenticatePlayer with wrong username throws IllegalArgumentException`() {
        val username = "george"
        val password = "MyNameIsGeorge"

        every { playerRepository.findPlayerByUsername(username) } returns null

        assertThrows<IllegalArgumentException> {
            service.authenticatePlayer(
                username,
                password,
            )
        }

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
    fun `updatePlayerDisplayName updates player displayName`() {
        val player = RegisteredPlayer(dummySession.playerId, dummySession.username!!, dummySession.displayName!!, "Password_hash")
        val updatedDisplayName = "newDummy"
        val updatedPlayer = player.copy(displayName = updatedDisplayName)

        every { playerRepository.getPlayerOrNull(player.id) } returns player
        every { playerRepository.updatePlayer(any()) } just Runs
        every { authTokenService.createToken(updatedPlayer) } returns dummyToken

        val token = service.updatePlayerDisplayName(dummySession, updatedDisplayName)
        assertEquals(dummyToken, token)

        verify(exactly = 1) {
            playerRepository.getPlayerOrNull(player.id)
            playerRepository.updatePlayer(updatedPlayer)
            authTokenService.createToken(updatedPlayer)
        }
    }

    @Test
    fun `updatePlayerDisplayName throws IllegalArgumentException if logged in player is guest`() {
        val playerId = UUID.randomUUID()
        val guestSession = PlayerSession(playerId, true, null, null)
        val updatedDisplayName = "newDummy"

        every { playerRepository.getPlayerOrNull(playerId) } returns GuestPlayer(playerId)

        val thrown = assertThrows<IllegalArgumentException> {
            service.updatePlayerDisplayName(guestSession, updatedDisplayName)
        }
        assertEquals("GuestPlayer cannot have display name", thrown.message)

        verify(exactly = 1) {
            playerRepository.getPlayerOrNull(playerId)
        }
    }

    @Test
    fun `updatePlayerPassword updates Player password`() {
        val oldPassword = "Password"
        val oldPasswordHash = "${oldPassword}_hash"
        val newPassword = "newPassword"
        val newPasswordHash = "${newPassword}_hash"
        val player = RegisteredPlayer(dummySession.playerId, "dummy", "", oldPasswordHash)
        val updatedPlayer = player.copy(password = newPasswordHash)

        every { playerRepository.getPlayerOrNull(dummySession.playerId) } returns player
        every { passwordHashService.verifyPassword(oldPasswordHash, oldPassword) } returns true
        every { passwordHashService.hashPassword(newPassword) } returns newPasswordHash
        every { playerRepository.updatePlayer(any()) } just Runs
        every { authTokenService.createToken(updatedPlayer) } returns dummyToken

        val token = service.updatePlayerPassword(dummySession, oldPassword, newPassword)
        assertEquals(dummyToken, token)

        verify (exactly = 1) {
            playerRepository.getPlayerOrNull(dummySession.playerId)
            passwordHashService.verifyPassword(oldPasswordHash, oldPassword)
            passwordHashService.hashPassword(newPassword)
            authTokenService.createToken(updatedPlayer)
            playerRepository.updatePlayer(updatedPlayer)
        }
    }

    @Test
    fun `updatePlayerPassword throws IllegalArgumentException if logged in player is guest`() {
        val oldPassword = "Password"
        val newPassword = "newPassword"
        val playerId = UUID.randomUUID()
        val guestSession = PlayerSession(playerId, true, null, null)

        every { playerRepository.getPlayerOrNull(playerId) } returns GuestPlayer(playerId)

        val thrown = assertThrows<IllegalArgumentException> {
            service.updatePlayerPassword(guestSession, oldPassword, newPassword)
        }
        assertEquals("GuestPlayer cannot have password", thrown.message)

        verify (exactly = 1) {
            playerRepository.getPlayerOrNull(playerId)
        }
    }

    @Test
    fun `updatePlayerPassword with wrong oldPassword throws IllegalArgumentException`() {
        val id = UUID.randomUUID()
        val username = "dummy"
        val password = "Password"
        val wrongPassword = "Password_hash"
        val newPassword = "newPassword"
        val playerToUpdatePassword = RegisteredPlayer(id, username, "Dummy", wrongPassword)

        every { playerRepository.getPlayerOrNull(id) } returns playerToUpdatePassword
        every { passwordHashService.verifyPassword(wrongPassword, password) } returns false

        val session = PlayerSession(id, false, username, "Dummy")
        val thrown = assertThrows<IllegalArgumentException> {
            service.updatePlayerPassword(session, password, newPassword)
        }
        assertEquals("Incorrect Password supplied", thrown.message)

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

        service.deletePlayer(session, id)

        assertEquals(GuestPlayer(id), player.captured)

        verify(exactly = 1) {
            playerRepository.getPlayerOrNull(id)
            playerRepository.updatePlayer(GuestPlayer(id))
        }
    }
}
