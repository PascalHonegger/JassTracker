package dev.honegger.endpoints

import dev.honegger.domain.Game
import dev.honegger.services.GameService
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import io.mockk.*
import kotlinx.datetime.LocalDateTime
import java.util.*
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class GameEndpointsTest {
    private val service = mockk<GameService>()

    @BeforeTest
    fun setup() {
        clearMocks(service)
        every { service.getGameOrNull(any(), any()) } returns null
    }

    @AfterTest
    fun teardown() {
        confirmVerified(service)
    }

    @Test
    fun `test get game finds dummy game`() = testApplication {
        application {
            installJson()
            configureGameEndpoints(service)
        }
        val client = createClient {
            installJson()
        }
        val dummyId = UUID.randomUUID()
        val dummyGame = Game(
            id = dummyId,
            startTime = LocalDateTime(2022, 4, 2, 13, 0, 0),
        )
        every {
            service.getGameOrNull(
                any(),
                dummyId
            )
        } returns dummyGame
        client.get("/api/games/$dummyId").apply {
            assertEquals(HttpStatusCode.OK, status)
            assertEquals("""{"id":"$dummyId","startTime":"2022-04-02T13:00:00Z","endTime":null}""", bodyAsText())
        }
        verify(exactly = 1) { service.getGameOrNull(any(), dummyId) }
    }

    @Test
    fun `test get game returns 404 if not found`() = testApplication {
        application {
            configureGameEndpoints(service)
        }

        client.get("/api/games/3de81ab0-792e-43b0-838b-acad78f29ba6").apply {
            assertEquals(HttpStatusCode.NotFound, status)
        }
        verify(exactly = 1) { service.getGameOrNull(any(), UUID.fromString("3de81ab0-792e-43b0-838b-acad78f29ba6")) }
    }
}
