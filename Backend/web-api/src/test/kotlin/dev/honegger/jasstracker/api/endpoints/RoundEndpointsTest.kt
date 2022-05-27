package dev.honegger.jasstracker.api.endpoints

import dev.honegger.jasstracker.domain.Round
import dev.honegger.jasstracker.domain.services.RoundService
import dev.honegger.jasstracker.domain.util.toUUID
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

    private fun ApplicationTestBuilder.setup(): HttpClient {
        application {
            installJson()
            installSecuredRoute { configureRoundEndpoints(service) }
        }
        return createClient {
            installJson()
            addJwtHeader()
        }
    }

    @Test
    fun `create round returns 201 and created round`() = testApplication {
        val client = setup()
        val dummyRound = Round(
            id = UUID.randomUUID(),
            number = 1,
            score = 42,
            gameId = UUID.randomUUID(),
            playerId = UUID.randomUUID(),
            contractId = UUID.randomUUID()
        )

        every { service.createRound(any(), any(), any(), any(), any(), any()) } returns dummyRound

        client.post("/rounds") {
            contentType(ContentType.Application.Json)
            setBody(
                WebCreateRound(
                    number = dummyRound.number,
                    score = dummyRound.score,
                    gameId = dummyRound.gameId,
                    playerId = dummyRound.playerId,
                    contractId = dummyRound.contractId
                )
            )
        }.apply {
            assertEquals(HttpStatusCode.Created, status)
            assertEquals(
                """{
                    |"id":"${dummyRound.id}",
                    |"number":${dummyRound.number},
                    |"score":${dummyRound.score},
                    |"gameId":"${dummyRound.gameId}",
                    |"playerId":"${dummyRound.playerId}",
                    |"contractId":"${dummyRound.contractId}"
                |}""".trimMargin().replace("\n", ""), bodyAsText())
        }
        verify(exactly = 1) {
            service.createRound(
                any(),
                dummyRound.number,
                dummyRound.score,
                dummyRound.gameId,
                dummyRound.playerId,
                dummyRound.contractId
            )
        }
    }

    @Test
    fun `update round returns 204`() = testApplication {
        val client = setup()
        val dummyRound = Round(
            id = UUID.randomUUID(),
            number = 1,
            score = 42,
            gameId = UUID.randomUUID(),
            playerId = UUID.randomUUID(),
            contractId = UUID.randomUUID()
        )

        every { service.updateRound(any(), any()) } just Runs

        client.put("/rounds/${dummyRound.id}") {
            contentType(ContentType.Application.Json)
            setBody(
                WebRound(
                    id = dummyRound.id,
                    number = dummyRound.number,
                    score = dummyRound.score,
                    gameId = dummyRound.gameId,
                    playerId = dummyRound.playerId,
                    contractId = dummyRound.contractId
                )
            )
        }.apply {
            assertEquals(HttpStatusCode.NoContent, status)
        }
        verify(exactly = 1) {
            service.updateRound(any(), dummyRound)
        }
    }

    @Test
    fun `delete round returns 404 if not found`() = testApplication {
        val client = setup()
        every { service.deleteRoundById(any(), any()) } returns false

        client.delete("/rounds/3de81ab0-792e-43b0-838b-acad78f29ba6").apply {
            assertEquals(HttpStatusCode.NotFound, status)
        }
        verify(exactly = 1) { service.deleteRoundById(any(), "3de81ab0-792e-43b0-838b-acad78f29ba6".toUUID()) }
    }

    @Test
    fun `delete round returns 200 if deleted`() = testApplication {
        val client = setup()
        every { service.deleteRoundById(any(), any()) } returns true

        client.delete("/rounds/3de81ab0-792e-43b0-838b-acad78f29ba6").apply {
            assertEquals(HttpStatusCode.OK, status)
        }
        verify(exactly = 1) { service.deleteRoundById(any(), "3de81ab0-792e-43b0-838b-acad78f29ba6".toUUID()) }
    }
}
