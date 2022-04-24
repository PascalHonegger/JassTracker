package dev.honegger.jasstracker.api.endpoints

import dev.honegger.jasstracker.domain.Game
import dev.honegger.jasstracker.domain.GameParticipant
import dev.honegger.jasstracker.domain.Team
import dev.honegger.jasstracker.domain.services.GameService
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
    }

    @AfterTest
    fun teardown() {
        confirmVerified(service)
    }

    @Test
    fun `get games finds empty list`() = testApplication {
        application {
            installJson()
            configureGameEndpoints(service)
        }
        val client = createClient {
            installJson()
        }

        every { service.getAllGames(any()) } returns emptyList()
        client.get("/api/games").apply {
            assertEquals(HttpStatusCode.OK, status)
            assertEquals("[]", bodyAsText())
        }
        verify(exactly = 1) { service.getAllGames(any()) }
    }

    @Test
    fun `get game finds dummy game`() = testApplication {
        application {
            installJson()
            configureGameEndpoints(service)
        }
        val client = createClient {
            installJson()
        }
        val dummyId = UUID.randomUUID()
        val p1Id = UUID.randomUUID()
        val p2Id = UUID.randomUUID()
        val p3Id = UUID.randomUUID()
        val p4Id = UUID.randomUUID()
        val dummyGame = Game(
            id = dummyId,
            startTime = LocalDateTime(2022, 4, 2, 13, 0, 0),
            rounds = emptyList(),
            team1 = Team(GameParticipant(p1Id, "p1"), GameParticipant(p2Id, "p2")),
            team2 = Team(GameParticipant(p3Id, "p3"), GameParticipant(p4Id, "p4")),
        )
        every {
            service.getGameOrNull(
                any(),
                dummyId
            )
        } returns dummyGame
        client.get("/api/games/$dummyId").apply {
            assertEquals(HttpStatusCode.OK, status)
            assertEquals(
                """{
                    |"id":"$dummyId",
                    |"startTime":"2022-04-02T13:00:00Z",
                    |"endTime":null,
                    |"rounds":[],
                    |"team1":{"player1":{"playerId":"$p1Id","displayName":"p1"},"player2":{"playerId":"$p2Id","displayName":"p2"}},
                    |"team2":{"player1":{"playerId":"$p3Id","displayName":"p3"},"player2":{"playerId":"$p4Id","displayName":"p4"}},
                    |"currentPlayer":{"playerId":"$p1Id","displayName":"p1"}
                |}""".trimMargin().replace("\n",""), bodyAsText())
        }
        verify(exactly = 1) { service.getGameOrNull(any(), dummyId) }
    }

    @Test
    fun `get game returns 404 if not found`() = testApplication {
        application {
            configureGameEndpoints(service)
        }
        every { service.getGameOrNull(any(), any()) } returns null

        client.get("/api/games/3de81ab0-792e-43b0-838b-acad78f29ba6").apply {
            assertEquals(HttpStatusCode.NotFound, status)
        }
        verify(exactly = 1) { service.getGameOrNull(any(), UUID.fromString("3de81ab0-792e-43b0-838b-acad78f29ba6")) }
    }

    @Test
    fun `delete game returns 404 if not found`() = testApplication {
        application {
            configureGameEndpoints(service)
        }
        every { service.deleteGameById(any(), any()) } returns false

        client.delete("/api/games/3de81ab0-792e-43b0-838b-acad78f29ba6").apply {
            assertEquals(HttpStatusCode.NotFound, status)
        }
        verify(exactly = 1) { service.deleteGameById(any(), UUID.fromString("3de81ab0-792e-43b0-838b-acad78f29ba6")) }
    }

    @Test
    fun `delete game returns 200 if deleted`() = testApplication {
        application {
            configureGameEndpoints(service)
        }
        every { service.deleteGameById(any(), any()) } returns true

        client.delete("/api/games/3de81ab0-792e-43b0-838b-acad78f29ba6").apply {
            assertEquals(HttpStatusCode.OK, status)
        }
        verify(exactly = 1) { service.deleteGameById(any(), UUID.fromString("3de81ab0-792e-43b0-838b-acad78f29ba6")) }
    }
}
