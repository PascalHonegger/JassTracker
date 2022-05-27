package dev.honegger.jasstracker.api.endpoints

import dev.honegger.jasstracker.domain.Game
import dev.honegger.jasstracker.domain.GameParticipation
import dev.honegger.jasstracker.domain.Round
import dev.honegger.jasstracker.domain.Team
import dev.honegger.jasstracker.domain.services.GameService
import dev.honegger.jasstracker.domain.util.toUUID
import io.ktor.client.*
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

    private fun ApplicationTestBuilder.setup(): HttpClient {
        application {
            installJson()
            installSecuredRoute { configureGameEndpoints(service) }
        }
        return createClient {
            installJson()
            addJwtHeader()
        }
    }

    @Test
    fun `get games finds empty list`() = testApplication {
        val client = setup()
        every { service.getAllGames(any()) } returns emptyList()
        client.get("/games").apply {
            assertEquals(HttpStatusCode.OK, status)
            assertEquals("[]", bodyAsText())
        }
        verify(exactly = 1) { service.getAllGames(any()) }
    }

    @Test
    fun `get game finds dummy game`() = testApplication {
        val client = setup()
        val dummyId = UUID.randomUUID()
        val p1Id = UUID.randomUUID()
        val p2Id = UUID.randomUUID()
        val p3Id = UUID.randomUUID()
        val p4Id = UUID.randomUUID()
        val dummyGame = Game(
            id = dummyId,
            startTime = LocalDateTime(2022, 4, 2, 13, 0, 0),
            rounds = emptyList(),
            team1 = Team(GameParticipation(p1Id, "p1"), GameParticipation(p2Id, "p2")),
            team2 = Team(GameParticipation(p3Id, "p3"), GameParticipation(p4Id, "p4")),
        )
        every {
            service.getGameOrNull(
                any(),
                dummyId
            )
        } returns dummyGame
        client.get("/games/$dummyId").apply {
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
                |}""".trimMargin().replace("\n", ""), bodyAsText())
        }
        verify(exactly = 1) { service.getGameOrNull(any(), dummyId) }
    }

    @Test
    fun `get game returns 404 if not found`() = testApplication {
        val client = setup()
        every { service.getGameOrNull(any(), any()) } returns null

        client.get("/games/3de81ab0-792e-43b0-838b-acad78f29ba6").apply {
            assertEquals(HttpStatusCode.NotFound, status)
        }
        verify(exactly = 1) { service.getGameOrNull(any(), "3de81ab0-792e-43b0-838b-acad78f29ba6".toUUID()) }
    }

    @Test
    fun `delete game returns 204 if deleted`() = testApplication {
        val client = setup()
        every { service.deleteGameById(any(), any()) } just Runs

        client.delete("/games/3de81ab0-792e-43b0-838b-acad78f29ba6").apply {
            assertEquals(HttpStatusCode.NoContent, status)
        }
        verify(exactly = 1) { service.deleteGameById(any(), "3de81ab0-792e-43b0-838b-acad78f29ba6".toUUID()) }
    }

    @Test
    fun `get game currentPlayer finds current player`() = testApplication {
        val client = setup()
        val dummyId = UUID.randomUUID()
        val p1Id = UUID.randomUUID()
        val p2Id = UUID.randomUUID()
        val p3Id = UUID.randomUUID()
        val p4Id = UUID.randomUUID()
        val dummyGame = Game(
            id = dummyId,
            startTime = LocalDateTime(2022, 4, 2, 13, 0, 0),
            rounds = emptyList(),
            team1 = Team(GameParticipation(p1Id, "p1"), GameParticipation(p2Id, "p2")),
            team2 = Team(GameParticipation(p3Id, "p3"), GameParticipation(p4Id, "p4")),
        )
        every {
            service.getGameOrNull(
                any(),
                dummyId
            )
        } returns dummyGame
        client.get("/games/$dummyId/currentPlayer").apply {
            assertEquals(HttpStatusCode.OK, status)
            assertEquals(
                """{"playerId":"$p1Id","displayName":"p1"}""", bodyAsText())
        }
        verify(exactly = 1) { service.getGameOrNull(any(), dummyId) }
    }

    @Test
    fun `get game currentPlayer finds current player even with multiple rounds played`() = testApplication {
        val client = setup()
        val dummyId = UUID.randomUUID()
        val p1Id = UUID.randomUUID()
        val p2Id = UUID.randomUUID()
        val p3Id = UUID.randomUUID()
        val p4Id = UUID.randomUUID()

        val round1 = Round(
            id = UUID.randomUUID(),
            number = 1,
            score = 150,
            gameId = dummyId,
            playerId = p1Id,
            contractId = UUID.randomUUID(),
        )
        val round2 = Round(
            id = UUID.randomUUID(),
            number = 2,
            score = 140,
            gameId = dummyId,
            playerId = p1Id,
            contractId = UUID.randomUUID(),
        )

        val dummyGame = Game(
            id = dummyId,
            startTime = LocalDateTime(2022, 4, 2, 13, 0, 0),
            rounds = listOf(round1, round2),
            team1 = Team(GameParticipation(p1Id, "p1"), GameParticipation(p2Id, "p2")),
            team2 = Team(GameParticipation(p3Id, "p3"), GameParticipation(p4Id, "p4")),
        )
        every {
            service.getGameOrNull(
                any(),
                dummyId
            )
        } returns dummyGame
        client.get("/games/$dummyId/currentPlayer").apply {
            assertEquals(HttpStatusCode.OK, status)
            assertEquals(
                """{"playerId":"$p2Id","displayName":"p2"}""", bodyAsText())
        }
        verify(exactly = 1) { service.getGameOrNull(any(), dummyId) }
    }
}
