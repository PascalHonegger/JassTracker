package dev.honegger.endpoints

import dev.honegger.domain.Round
import dev.honegger.services.RoundService
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

class RoundEndpointsTest {
    private val service = mockk<RoundService>()

    @BeforeTest
    fun setup() {
        clearMocks(service)
    }

    @AfterTest
    fun teardown() {
        confirmVerified(service)
    }

    @Test
    fun `test get rounds of game returns all all rounds of the game`() = testApplication {
        application {
            installJson()
            configureRoundEndpoints(service)
        }
        val client = createClient {
            installJson()
        }

        val id1 = UUID.randomUUID()
        val id2 = UUID.randomUUID()
        val gameId = UUID.randomUUID()
        val playerId = UUID.randomUUID()
        val contractId1 = UUID.randomUUID()
        val contractId2 = UUID.randomUUID()

        val round1 = Round(
            id = id1,
            number = 1,
            score = 150,
            gameId = gameId,
            playerId = playerId,
            contractId = contractId1,
        )
        val round2 = Round(
            id = id2,
            number = 2,
            score = 140,
            gameId = gameId,
            playerId = playerId,
            contractId = contractId2,
        )
        every {
            service.getRounds(any(), gameId)
        } returns listOf(round1, round2)

        client.get("/api/rounds/byGame/$gameId").apply {
            assertEquals(HttpStatusCode.OK, status)
            assertEquals(
                """[{"id":"$id1","number":1,"score":150,"gameId":"$gameId","playerId":"$playerId","contractId":"$contractId1"},{"id":"$id2","number":2,"score":140,"gameId":"$gameId","playerId":"$playerId","contractId":"$contractId2"}]""",
                bodyAsText()
            )
        }
        verify(exactly = 1) { service.getRounds(any(), any()) }
    }
}
