package dev.honegger.endpoints

import dev.honegger.domain.Game
import dev.honegger.domain.Table
import dev.honegger.services.TableService
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
            assertEquals("""{"id":"$id","name":"dummy","ownerId":"$ownerId","gameIds":[]}""", bodyAsText())
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
                    rounds = emptyList()
                )
            ),
        )
        every {
            service.getTables(any())
        } returns listOf(dummyTable1, dummyTable2)

        client.get("/api/tables").apply {
            assertEquals(HttpStatusCode.OK, status)
            assertEquals(
                """[{"id":"$id1","name":"dummy1","ownerId":"$owner1UUID","gameIds":[]},{"id":"$id2","name":"dummy2","ownerId":"$owner2UUID","gameIds":["$gameId"]}]""",
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
