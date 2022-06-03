package dev.honegger.jasstracker.api.endpoints

import dev.honegger.jasstracker.domain.Game
import dev.honegger.jasstracker.domain.GameParticipation
import dev.honegger.jasstracker.domain.Round
import dev.honegger.jasstracker.domain.Team
import dev.honegger.jasstracker.domain.services.CreateGameParticipation
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
            service.getGame(
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
                |}""".trimMargin().replace("\n", ""), bodyAsText()
            )
        }
        verify(exactly = 1) { service.getGame(any(), dummyId) }
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
            service.getGame(
                any(),
                dummyId
            )
        } returns dummyGame
        client.get("/games/$dummyId/currentPlayer").apply {
            assertEquals(HttpStatusCode.OK, status)
            assertEquals(
                """{"playerId":"$p1Id","displayName":"p1"}""", bodyAsText()
            )
        }
        verify(exactly = 1) { service.getGame(any(), dummyId) }
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
            service.getGame(
                any(),
                dummyId
            )
        } returns dummyGame
        client.get("/games/$dummyId/currentPlayer").apply {
            assertEquals(HttpStatusCode.OK, status)
            assertEquals(
                """{"playerId":"$p2Id","displayName":"p2"}""", bodyAsText()
            )
        }
        verify(exactly = 1) { service.getGame(any(), dummyId) }
    }

    @Test
    fun `createGame creates game`() = testApplication {
        val client = setup()
        val tableId = UUID.randomUUID()
        val gameId = UUID.randomUUID()
        val t1p1Id = dummySession.playerId
        val t1p2Id = UUID.randomUUID()
        val t2p1Id = UUID.randomUUID()
        val t2p2Id = UUID.randomUUID()

        every {
            service.createGame(
                dummySession,
                tableId,
                CreateGameParticipation(t1p1Id, "T1P1"),
                CreateGameParticipation(null, "T1P2"),
                CreateGameParticipation(null, "T2P1"),
                CreateGameParticipation(null, "T2P2")
            )
        } returns Game(
            gameId,
            LocalDateTime(2022, 4, 20, 13, 37),
            null,
            emptyList(),
            Team(GameParticipation(t1p1Id, "T1P1"), GameParticipation(t1p2Id, "T1P2")),
            Team(GameParticipation(t2p1Id, "T2P1"), GameParticipation(t2p2Id, "T2P2")),
        )

        client.post("/games") {
            contentType(ContentType.Application.Json)
            setBody(
                WebCreateGame(
                    tableId,
                    WebCreateGameParticipation(t1p1Id, "T1P1"),
                    WebCreateGameParticipation(null, "T1P2"),
                    WebCreateGameParticipation(null, "T2P1"),
                    WebCreateGameParticipation(null, "T2P2"),
                )
            )
        }.apply {
            assertEquals(HttpStatusCode.Created, status)
            assertEquals(
                """
                    |{
                        |"id":"$gameId",
                        |"startTime":"2022-04-20T13:37:00Z",
                        |"endTime":null,
                        |"rounds":[],
                        |"team1":{
                            |"player1":{
                                |"playerId":"$t1p1Id",
                                |"displayName":"T1P1"
                            |},
                            |"player2":{
                                |"playerId":"$t1p2Id",
                                |"displayName":"T1P2"
                            |}
                        |},
                        |"team2":{
                            |"player1":{
                                |"playerId":"$t2p1Id",
                                |"displayName":"T2P1"
                            |},
                            |"player2":{
                                |"playerId":"$t2p2Id",
                                |"displayName":"T2P2"
                            |}
                        |},
                        |"currentPlayer":{
                            |"playerId":"$t1p1Id",
                            |"displayName":"T1P1"
                        |}
                    |}
                """.trimMargin().replace("\n", ""),
                bodyAsText()
            )
        }
        verify(exactly = 1) {
            service.createGame(
                dummySession,
                tableId,
                CreateGameParticipation(t1p1Id, "T1P1"),
                CreateGameParticipation(null, "T1P2"),
                CreateGameParticipation(null, "T2P1"),
                CreateGameParticipation(null, "T2P2")
            )
        }
    }
}
