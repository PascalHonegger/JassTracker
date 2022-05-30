package dev.honegger.jasstracker.api.endpoints

import dev.honegger.jasstracker.domain.*
import dev.honegger.jasstracker.domain.services.StatisticsService
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

class StatisticsEndpointTest {
    private val service = mockk<StatisticsService>()

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
            installSecuredRoute { configureStatisticsEndpoint(service) }
        }
        return createClient {
            installJson()
            addJwtHeader()
        }
    }

    @Test
    fun `get game statistics`() = testApplication {
        val client = setup()

        val gameId = UUID.randomUUID()
        val playerId = UUID.randomUUID()

        every { service.getGameStatistics(any(), any()) } returns GameStatistics(
            playerAverages = listOf(PlayerAverage(GameParticipation(playerId, "David"), Average(42.5), Average(40.1))),
            teamAverages = TeamAverages(Average(100.5), Average(130.025)),
            weightedTeamAverages = TeamAverages(Average(101.5), Average(131.025)),
            expectedScoresOverTime = listOf(TeamScores(Score(30), Score(157))),
        )

        client.get("/statistics/game/$gameId").apply {
            assertEquals(HttpStatusCode.OK, status)
            assertEquals(
                """{
                    |"playerAverages":[
                        |{"playerId":"$playerId","displayName":"David","average":42.5,"weightedAverage":40.1}
                    |],
                    |"team1Average":100.5,
                    |"team2Average":130.025,
                    |"team1WeightedAverage":101.5,
                    |"team2WeightedAverage":131.025,
                    |"expectedScoresOverTime":[{"team1Score":30,"team2Score":157}]
                |}""".trimMargin().replace("\n", ""), bodyAsText()
            )
        }
        verify(exactly = 1) {
            service.getGameStatistics(any(), gameId)
        }
    }

    @Test
    fun `get player statistics`() = testApplication {
        val client = setup()

        val playerId = UUID.randomUUID()
        val contractId = UUID.randomUUID()

        every { service.getPlayerStatistics(any(), any()) } returns PlayerStatistics(
            average = Average(105.9),
            total = Score(4206),
            contractAverages = mapOf(contractId to Average(42.0)),
            scoreDistribution = mapOf(Score(42) to 2)
        )

        client.get("/statistics/player/$playerId").apply {
            assertEquals(HttpStatusCode.OK, status)
            assertEquals(
                """{
                    |"average":105.9,
                    |"total":4206,
                    |"contractAverages":{"$contractId":42.0},
                    |"scoreDistribution":{"42":2}
                |}""".trimMargin().replace("\n", ""), bodyAsText()
            )
        }
        verify(exactly = 1) {
            service.getPlayerStatistics(any(), playerId)
        }
    }

    @Test
    fun `get table statistics`() = testApplication {
        val client = setup()

        val tableId = UUID.randomUUID()
        val gameId = UUID.randomUUID()
        val playerId = UUID.randomUUID()
        val contractId = UUID.randomUUID()

        every { service.getTableStatistics(any(), any()) } returns TableStatistics(
            playerAverages = listOf(PlayerAverage(GameParticipation(playerId, "David"), Average(123.456), Average(100.23))),
            contractAverages = mapOf(contractId to Average(42.0)),
            scoresOverTime = listOf(GameScoreSummary(gameId, TeamScores(Score(5600), Score(7122))))
        )

        client.get("/statistics/table/$tableId").apply {
            assertEquals(HttpStatusCode.OK, status)
            assertEquals(
                """{
                    |"playerAverages":[
                        |{"playerId":"$playerId","displayName":"David","average":123.456,"weightedAverage":100.23}
                    |],
                    |"contractAverages":{"$contractId":42.0},
                    |"scoresOverTime":[{"gameId":"$gameId","total":{"team1Score":5600,"team2Score":7122}}]
                |}""".trimMargin().replace("\n", ""), bodyAsText()
            )
        }
        verify(exactly = 1) {
            service.getTableStatistics(any(), tableId)
        }
    }

}
