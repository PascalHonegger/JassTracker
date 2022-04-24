package dev.honegger.jasstracker.api.endpoints

import dev.honegger.jasstracker.domain.GuestPlayer
import dev.honegger.jasstracker.domain.RegisteredPlayer
import dev.honegger.jasstracker.domain.services.PlayerService
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

    @Test
    fun `get registered player finds dummy player`() = testApplication {
        application {
            installJson()
            configurePlayerEndpoints(service)
        }
        val client = createClient {
            installJson()
        }
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
        client.get("/api/players/$dummyId").apply {
            assertEquals(HttpStatusCode.OK, status)
            assertEquals("""{"id":"$dummyId","username":"bar","displayName":"foo","password":null,"isGuest":false}""", bodyAsText())
        }
        verify(exactly = 1) { service.getPlayerOrNull(any(), dummyId) }
    }

    @Test
    fun `get guest player finds dummy guest`() = testApplication {
        application {
            installJson()
            configurePlayerEndpoints(service)
        }
        val client = createClient {
            installJson()
        }
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
        client.get("/api/players/$guestId").apply {
            assertEquals(HttpStatusCode.OK, status)
            assertEquals("""{"id":"$guestId","username":null,"displayName":null,"password":null,"isGuest":true}""", bodyAsText())
        }
        verify(exactly = 1) { service.getPlayerOrNull(any(), guestId) }
    }

    @Test
    fun `get game returns 404 if not found`() = testApplication {
        application {
            configurePlayerEndpoints(service)
        }

        client.get("/api/players/3de81ab0-792e-43b0-838b-acad78f29ba6").apply {
            assertEquals(HttpStatusCode.NotFound, status)
        }
        verify(exactly = 1) { service.getPlayerOrNull(any(), UUID.fromString("3de81ab0-792e-43b0-838b-acad78f29ba6")) }
    }
}
