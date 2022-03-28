package dev.honegger.endpoints

import dev.honegger.domain.Scoreboard
import dev.honegger.services.ScoreboardService
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.plugins.*
import io.ktor.server.testing.*
import io.mockk.*
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.assertThrows
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class ScoreboardEndpointsTest {
    private val service = mockk<ScoreboardService>()

    @BeforeTest
    fun setup() {
        clearMocks(service)
        every { service.getScoreboardOrNull(any(), any()) } returns null
    }

    @AfterTest
    fun teardown() {
        confirmVerified(service)
    }

    @Test
    fun `test get scoreboard finds dummy scoreboard`() = testApplication {
        application {
            configureScoreboardEndpoints(service)
        }
        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }
        val dummyScoreboard = Scoreboard(
            id = "dummy",
            name = "dummy",
            ownerId = "dummy",
        )
        every {
            service.getScoreboardOrNull(
                any(),
                "dummy"
            )
        } returns dummyScoreboard
        client.get("/api/scoreboards/dummy").apply {
            assertEquals(HttpStatusCode.OK, status)
            assertEquals("""{"name":"dummy","ownerId":"dummy"}""", bodyAsText())
        }
        verify(exactly = 1) { service.getScoreboardOrNull(any(), "dummy") }
    }

    @Test
    fun `test get scoreboard returns 404 if not found`() = testApplication {
        application {
            configureScoreboardEndpoints(service)
        }

        val exception = assertThrows<ClientRequestException> {
            runBlocking {
                client.get("/api/scoreboards/whatever")
            }
        }
        assertEquals(HttpStatusCode.NotFound, exception.response.status)
        verify(exactly = 1) { service.getScoreboardOrNull(any(), "whatever") }
    }
}
