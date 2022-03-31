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
        val dummyTable = Table(
            id = "dummy",
            name = "dummy",
            ownerId = "dummy",
        )
        every {
            service.getTableOrNull(
                any(),
                "dummy"
            )
        } returns dummyTable
        client.get("/api/tables/dummy").apply {
            assertEquals(HttpStatusCode.OK, status)
            assertEquals("""{"name":"dummy","ownerId":"dummy"}""", bodyAsText())
        }
        verify(exactly = 1) { service.getTableOrNull(any(), "dummy") }
    }

    @Test
    fun `test get table returns 404 if not found`() = testApplication {
        application {
            configureTableEndpoints(service)
        }

        val exception = assertThrows<ClientRequestException> {
            runBlocking {
                client.get("/api/tables/whatever")
            }
        }
        assertEquals(HttpStatusCode.NotFound, exception.response.status)
        verify(exactly = 1) { service.getTableOrNull(any(), "whatever") }
    }
}
