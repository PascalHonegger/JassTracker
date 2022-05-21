package dev.honegger.jasstracker.api.endpoints

import dev.honegger.jasstracker.domain.services.RoundService
import dev.honegger.jasstracker.domain.util.toUUID
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.server.testing.*
import io.mockk.*
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
