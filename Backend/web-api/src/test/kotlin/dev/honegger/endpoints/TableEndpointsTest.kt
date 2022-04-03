package dev.honegger.endpoints

import dev.honegger.domain.Table
import dev.honegger.services.TableService
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
            configureTableEndpoints(service)
        }
        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
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
            assertEquals("""{"id":"$id","name":"dummy","ownerId":"$ownerId","games":[]}""", bodyAsText())
        }
        verify(exactly = 1) { service.getTableOrNull(any(), id) }
    }

    @Test
    fun `test get tables finds multiple tables`() = testApplication {
        application {
            configureTableEndpoints(service)
        }
        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }
        val owner1UUID = UUID.randomUUID()
        val owner2UUID = UUID.randomUUID()
        val id1 = UUID.randomUUID()
        val id2 = UUID.randomUUID()
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
            games = emptyList(),
        )
        every {
            service.getTables(any())
        } returns listOf(dummyTable1, dummyTable2)

        client.get("/api/tables").apply {
            assertEquals(HttpStatusCode.OK, status)
            assertEquals(
                """[{"id":"$id1","name":"dummy1","ownerId":"$owner1UUID","games":[]},{"id":"$id2","name":"dummy2","ownerId":"$owner2UUID","games":[]}]""",
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

        val exception = assertThrows<ClientRequestException> {
            runBlocking {
                client.get("/api/tables/3de81ab0-792e-43b0-838b-acad78f29ba6")
            }
        }
        assertEquals(HttpStatusCode.NotFound, exception.response.status)
        verify(exactly = 1) { service.getTableOrNull(any(), UUID.fromString("3de81ab0-792e-43b0-838b-acad78f29ba6")) }
    }
}
