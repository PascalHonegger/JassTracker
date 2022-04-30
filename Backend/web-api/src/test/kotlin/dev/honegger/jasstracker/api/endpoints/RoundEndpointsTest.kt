package dev.honegger.jasstracker.api.endpoints

import dev.honegger.jasstracker.domain.Round
import dev.honegger.jasstracker.domain.services.RoundService
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
    fun `get rounds of game returns all rounds of the game`() = testApplication {
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

    @Test
    fun `get rounds of game returns empty list if game not found`() = testApplication {
        application {
            installJson()
            configureRoundEndpoints(service)
        }
        val client = createClient {
            installJson()
        }

        every {
            service.getRounds(any(), any())
        } returns emptyList()

        client.get("/api/rounds/byGame/84c532b1-dd87-4ca0-bc85-81c9c5d51c21").apply {
            assertEquals(HttpStatusCode.OK, status)
            assertEquals("[]", bodyAsText())
        }
        verify(exactly = 1) { service.getRounds(any(), UUID.fromString("84c532b1-dd87-4ca0-bc85-81c9c5d51c21")) }
    }

    @Test
    fun `delete round returns 404 if not found`() = testApplication {
        application {
            configureRoundEndpoints(service)
        }
        every { service.deleteRoundById(any(), any()) } returns false

        client.delete("/api/rounds/3de81ab0-792e-43b0-838b-acad78f29ba6").apply {
            assertEquals(HttpStatusCode.NotFound, status)
        }
        verify(exactly = 1) { service.deleteRoundById(any(), UUID.fromString("3de81ab0-792e-43b0-838b-acad78f29ba6")) }
    }

    @Test
    fun `delete round returns 200 if deleted`() = testApplication {
        application {
            configureRoundEndpoints(service)
        }
        every { service.deleteRoundById(any(), any()) } returns true

        client.delete("/api/rounds/3de81ab0-792e-43b0-838b-acad78f29ba6").apply {
            assertEquals(HttpStatusCode.OK, status)
        }
        verify(exactly = 1) { service.deleteRoundById(any(), UUID.fromString("3de81ab0-792e-43b0-838b-acad78f29ba6")) }
    }
}
