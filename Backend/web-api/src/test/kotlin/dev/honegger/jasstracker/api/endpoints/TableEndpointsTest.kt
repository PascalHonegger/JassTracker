package dev.honegger.jasstracker.api.endpoints

import dev.honegger.jasstracker.domain.Game
import dev.honegger.jasstracker.domain.GameParticipant
import dev.honegger.jasstracker.domain.Table
import dev.honegger.jasstracker.domain.Team
import dev.honegger.jasstracker.domain.services.TableService
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

class TableEndpointsTest {
    private val service = mockk<TableService>()

    @BeforeTest
    fun setup() {
        clearMocks(service)
        every { service.getTableOrNull(any(), any()) } returns null
    }

    @AfterTest
    fun teardown() {
        confirmVerified(service)
    }

    @Test
    fun `test get table finds dummy table`() = testApplication {
        application {
            installJson()
            configureTableEndpoints(service)
        }
        val client = createClient {
            installJson()
        }
        val id = UUID.randomUUID()
        val ownerId = UUID.randomUUID()
        val dummyTable = Table(
            id = id,
            name = "dummy",
            ownerId = ownerId,
            games = emptyList()
        )
        every {
            service.getTableOrNull(
                any(),
                id
            )
        } returns dummyTable
        client.get("/api/tables/$id").apply {
            assertEquals(HttpStatusCode.OK, status)
            assertEquals("""{"id":"$id","name":"dummy","ownerId":"$ownerId","gameIds":[],"latestGame":null}""", bodyAsText())
        }
        verify(exactly = 1) { service.getTableOrNull(any(), id) }
    }

    @Test
    fun `test get tables finds multiple tables`() = testApplication {
        application {
            installJson()
            configureTableEndpoints(service)
        }
        val client = createClient {
            installJson()
        }
        val owner1UUID = UUID.randomUUID()
        val owner2UUID = UUID.randomUUID()
        val id1 = UUID.randomUUID()
        val id2 = UUID.randomUUID()
        val gameId = UUID.randomUUID()
        val p1Id = UUID.randomUUID()
        val p2Id = UUID.randomUUID()
        val p3Id = UUID.randomUUID()
        val p4Id = UUID.randomUUID()
        val dummyTable1 = Table(
            id = id1,
            name = "dummy1",
            ownerId = owner1UUID,
            games = emptyList(),
        )
        val dummyTable2 = Table(
            id = id2,
            name = "dummy2",
            ownerId = owner2UUID,
            games = listOf(
                Game(
                    id = gameId,
                    startTime = LocalDateTime(2022, 4, 10, 20, 20, 0, 0),
                    endTime = null,
                    rounds = emptyList(),
                    team1 = Team(GameParticipant(p1Id, "p1"), GameParticipant(p2Id, "p2")),
                    team2 = Team(GameParticipant(p3Id, "p3"), GameParticipant(p4Id, "p4")),
                )
            ),
        )
        every {
            service.getTables(any())
        } returns listOf(dummyTable1, dummyTable2)

        client.get("/api/tables").apply {
            assertEquals(HttpStatusCode.OK, status)
            assertEquals(
                """[
                    |{
                        |"id":"$id1",
                        |"name":"dummy1",
                        |"ownerId":"$owner1UUID",
                        |"gameIds":[],
                        |"latestGame":null
                    |},
                    |{
                        |"id":"$id2",
                        |"name":"dummy2",
                        |"ownerId":"$owner2UUID",
                        |"gameIds":["$gameId"],
                        |"latestGame":{
                            |"id":"$gameId",
                            |"startTime":"2022-04-10T20:20:00Z",
                            |"endTime":null,
                            |"rounds":[],
                            |"team1":{
                                |"player1":{"playerId":"$p1Id","displayName":"p1"},
                                |"player2":{"playerId":"$p2Id","displayName":"p2"}},
                            |"team2":{
                                |"player1":{"playerId":"$p3Id","displayName":"p3"},
                                |"player2":{"playerId":"$p4Id","displayName":"p4"}
                            |}
                        |}
                    |}]""".trimMargin().replace("\n",""),
                bodyAsText()
            )
        }
        verify(exactly = 1) { service.getTables(any()) }
    }

    @Test
    fun `test get table returns 404 if not found`() = testApplication {
        application {
            configureTableEndpoints(service)
        }

        client.get("/api/tables/3de81ab0-792e-43b0-838b-acad78f29ba6").apply {
            assertEquals(HttpStatusCode.NotFound, status)
        }
        verify(exactly = 1) { service.getTableOrNull(any(), UUID.fromString("3de81ab0-792e-43b0-838b-acad78f29ba6")) }
    }
}
