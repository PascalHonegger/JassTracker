package dev.honegger.jasstracker.api.endpoints

import dev.honegger.jasstracker.domain.services.AuthToken
import dev.honegger.jasstracker.domain.services.PlayerService
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.routing.*
import io.ktor.server.testing.*
import io.mockk.*
import kotlin.test.*

class AuthenticationEndpointsTest {

    private val playerService = mockk<PlayerService>()

    @BeforeTest
    fun setup() {
        clearMocks(playerService)
    }

    @AfterTest
    fun teardown() {
        confirmVerified(playerService)
    }

    private fun ApplicationTestBuilder.setup(): HttpClient {
        application {
            installJson()
            routing { configureAuthenticationEndpoints(playerService) }
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
        } returns AuthToken("dummyToken")

        client.post("/login") {
            contentType(ContentType.Application.Json)
            setBody(WebLogin("dummy", "password"))
        }.apply {
            assertEquals(HttpStatusCode.OK, status)
            assertEquals("""{"token":"dummyToken"}""", bodyAsText())
        }

        verify(exactly = 1) {
            playerService.authenticatePlayer("dummy", "password")
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
        every { playerService.registerGuestPlayer() } returns AuthToken("dummyToken")
        client.post("/guest-access") {
            contentType(ContentType.Application.Json)
        }.apply {
            assertEquals(HttpStatusCode.OK, status)
            assertEquals("""{"token":"dummyToken"}""", bodyAsText())
        }

        verify(exactly = 1) {
            playerService.registerGuestPlayer()
        }
    }
}
