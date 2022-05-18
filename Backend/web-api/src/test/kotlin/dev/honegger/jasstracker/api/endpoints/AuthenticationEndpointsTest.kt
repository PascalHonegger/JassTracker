package dev.honegger.jasstracker.api.endpoints

import dev.honegger.jasstracker.domain.GuestPlayer
import dev.honegger.jasstracker.domain.RegisteredPlayer
import dev.honegger.jasstracker.domain.services.AuthTokenService
import dev.honegger.jasstracker.domain.services.PlayerService
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.routing.*
import io.ktor.server.testing.*
import io.mockk.*
import java.util.*
import kotlin.test.*

class AuthenticationEndpointsTest {

    private val playerService = mockk<PlayerService>()
    private val authService = mockk<AuthTokenService>()

    @BeforeTest
    fun setup() {
        clearMocks(playerService, authService)
    }

    @AfterTest
    fun teardown() {
        confirmVerified(playerService, authService)
    }

    private fun ApplicationTestBuilder.setup(): HttpClient {
        application {
            installJson()
            routing { configureAuthenticationEndpoints(playerService, authService) }
        }
        return createClient {
            installJson()
        }
    }

    @Test
    fun `post login returns playerToken`() = testApplication {
        val client = setup()

        every {
            playerService.authenticatePlayer(any(), any())
        } returns RegisteredPlayer(
            id = UUID.randomUUID(),
            username = "dummy",
            displayName = "Dummy",
            password = "<redacted>"
        )

        every { authService.createToken(any()) } returns "dummyToken"

        client.post("/login") {
            contentType(ContentType.Application.Json)
            setBody(WebLogin("dummy", "password"))
        }.apply {
            assertEquals(HttpStatusCode.OK, status)
            assertEquals("""{"token":"dummyToken"}""", bodyAsText())
        }

        verify(exactly = 1) {
            playerService.authenticatePlayer("dummy", "password")
            authService.createToken(match { it is RegisteredPlayer && it.username == "dummy" })
        }
    }

    @Test
    fun `post login returns badRequest if player is null`() = testApplication {
        val client = setup()
        every { playerService.authenticatePlayer(any(), any()) } returns null
        client.post("/login") {
            contentType(ContentType.Application.Json)
            setBody(WebLogin("username", "password"))
        }.apply {
            assertEquals(HttpStatusCode.BadRequest, status)
        }
        verify(exactly = 1) {
            playerService.authenticatePlayer("username", "password")
        }
    }

    @Test
    fun `post guestAccess returns guestToken`() = testApplication {
        val client = setup()
        val guestId = UUID.randomUUID()
        every { playerService.registerGuestPlayer() } returns GuestPlayer(guestId)
        every { authService.createToken(any()) } returns "dummyToken"
        client.post("/guest-access") {
            contentType(ContentType.Application.Json)
        }.apply {
            assertEquals(HttpStatusCode.OK, status)
            assertEquals("""{"token":"dummyToken"}""", bodyAsText())
        }

        verify(exactly = 1) {
            playerService.registerGuestPlayer()
            authService.createToken(match { it is GuestPlayer && it.id == guestId })
        }
    }
}
