package dev.honegger.endpoints

import dev.honegger.domain.Player
import dev.honegger.services.PlayerService
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
    fun `test get player finds dummy player`() = testApplication {
        application {
            installJson()
            configurePlayerEndpoints(service)
        }
        val client = createClient {
            installJson()
        }
        val dummyId = UUID.randomUUID()
        val dummyPlayer = Player(
            id = dummyId,
            username = "bar",
            displayName = "foo",
            password = "max-security",
            isGuest = false,
        )
        every {
            service.getPlayerOrNull(
                any(),
                dummyId
            )
        } returns dummyPlayer
        client.get("/api/players/$dummyId").apply {
            assertEquals(HttpStatusCode.OK, status)
            assertEquals("""{"id":"$dummyId","username":"bar","displayName":"foo","password":"","isGuest":false}""", bodyAsText())
        }
        verify(exactly = 1) { service.getPlayerOrNull(any(), dummyId) }
    }

    @Test
    fun `test get game returns 404 if not found`() = testApplication {
        application {
            configurePlayerEndpoints(service)
        }

        client.get("/api/players/3de81ab0-792e-43b0-838b-acad78f29ba6").apply {
            assertEquals(HttpStatusCode.NotFound, status)
        }
        verify(exactly = 1) { service.getPlayerOrNull(any(), UUID.fromString("3de81ab0-792e-43b0-838b-acad78f29ba6")) }
    }
}
