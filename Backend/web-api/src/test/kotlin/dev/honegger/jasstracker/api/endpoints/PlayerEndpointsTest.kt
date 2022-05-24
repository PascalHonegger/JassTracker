package dev.honegger.jasstracker.api.endpoints

import dev.honegger.jasstracker.domain.GuestPlayer
import dev.honegger.jasstracker.domain.RegisteredPlayer
import dev.honegger.jasstracker.domain.services.AuthToken
import dev.honegger.jasstracker.domain.services.PlayerService
import dev.honegger.jasstracker.domain.util.toUUID
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import io.mockk.*
import java.util.*
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class PlayerEndpointsTest {
    private val service = mockk<PlayerService>()

    @BeforeTest
    fun setup() {
        clearMocks(service)
        every { service.getPlayerOrNull(any(), any()) } returns null
    }

    @AfterTest
    fun teardown() {
        confirmVerified(service)
    }

    private fun ApplicationTestBuilder.setup(): HttpClient {
        application {
            installJson()
            installSecuredRoute { configurePlayerEndpoints(service) }
        }
        return createClient {
            installJson()
            addJwtHeader()
        }
    }

    @Test
    fun `get registered player finds dummy player`() = testApplication {
        val client = setup()
        val dummyId = UUID.randomUUID()
        val dummyPlayer = RegisteredPlayer(
            id = dummyId,
            username = "bar",
            displayName = "foo",
            password = "max-security",
        )
        every {
            service.getPlayerOrNull(
                any(),
                dummyId
            )
        } returns dummyPlayer
        client.get("/players/$dummyId").apply {
            assertEquals(HttpStatusCode.OK, status)
            assertEquals("""{"id":"$dummyId","username":"bar","displayName":"foo","password":null,"isGuest":false}""",
                bodyAsText())
        }
        verify(exactly = 1) { service.getPlayerOrNull(any(), dummyId) }
    }

    @Test
    fun `get guest player finds dummy guest`() = testApplication {
        val client = setup()
        val guestId = UUID.randomUUID()
        val guest = GuestPlayer(
            id = guestId,
        )
        every {
            service.getPlayerOrNull(
                any(),
                guestId
            )
        } returns guest
        client.get("/players/$guestId").apply {
            assertEquals(HttpStatusCode.OK, status)
            assertEquals("""{"id":"$guestId","username":null,"displayName":null,"password":null,"isGuest":true}""",
                bodyAsText())
        }
        verify(exactly = 1) { service.getPlayerOrNull(any(), guestId) }
    }

    @Test
    fun `get player returns 404 if not found`() = testApplication {
        val client = setup()
        client.get("/players/3de81ab0-792e-43b0-838b-acad78f29ba6").apply {
            assertEquals(HttpStatusCode.NotFound, status)
        }
        verify(exactly = 1) { service.getPlayerOrNull(any(), "3de81ab0-792e-43b0-838b-acad78f29ba6".toUUID()) }
    }

    @Test
    fun `delete player calls delete player`() = testApplication {
        val client = setup()
        val dummyId = UUID.randomUUID()
        val dummyPlayer = RegisteredPlayer(
            id = dummyId,
            username = "bar",
            displayName = "foo",
            password = "max-security",
        )

        every {
            service.getPlayerOrNull(any(), dummyId)
        } returns dummyPlayer

        every {
            service.deletePlayer(any(), dummyPlayer)
        } just Runs

        client.delete("/players/$dummyId").apply {
            assertEquals(HttpStatusCode.OK, status)
        }

        verify(exactly = 1) {
            service.getPlayerOrNull(any(), dummyId)
            service.deletePlayer(any(), dummyPlayer)
        }
    }

    @Test
    fun `delete player returns 404 if not found`() = testApplication {
        val client = setup()
        val dummyId = UUID.randomUUID()

        client.delete("/players/$dummyId").apply {
            assertEquals(HttpStatusCode.NotFound, status)
        }

        verify(exactly = 1) {
            service.getPlayerOrNull(any(), dummyId)
        }
    }

    @Test
    fun `updatePlayerDisplayName returns OK if could update`() = testApplication {
        val client = setup()

        val dummyId = UUID.randomUUID()
        val authToken = AuthToken("secureeeee")

        every {
            service.updatePlayerDisplayName(any(), "Bar")
        } returns authToken

        client.put("/players/$dummyId/displayName") {
            contentType(ContentType.Application.Json)
            setBody(DisplayNameRequest("Bar"))
        }.apply {
            assertEquals(HttpStatusCode.OK, status)
            assertEquals("""{"token":"${authToken.token}"}""", bodyAsText())
        }

        verify(exactly = 1) {
            service.updatePlayerDisplayName(any(), "Bar")
        }
    }
}
