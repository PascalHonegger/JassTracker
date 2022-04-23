package dev.honegger.jasstracker.api.endpoints

import dev.honegger.jasstracker.domain.Contract
import dev.honegger.jasstracker.domain.ContractType
import dev.honegger.jasstracker.domain.services.ContractService
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

class ContractEndpointsTest {
    private val service = mockk<ContractService>()

    @BeforeTest
    fun setup() {
        clearMocks(service)
    }

    @AfterTest
    fun teardown() {
        confirmVerified(service)
    }

    @Test
    fun `test get contracts returns all contracts`() = testApplication {
        application {
            installJson()
            configureContractEndpoints(service)
        }
        val client = createClient {
            installJson()
        }
        val dummyId = UUID.randomUUID()
        val acorns = Contract(
            id = dummyId,
            name = "Eichle",
            multiplier = 1,
            type = ContractType.Acorns
        )
        every { service.getContracts(any()) } returns listOf(acorns)
        client.get("/api/contracts").apply {
            assertEquals(HttpStatusCode.OK, status)
            assertEquals("""[{"id":"$dummyId","name":"Eichle","multiplier":1,"type":"Acorns"}]""", bodyAsText())
        }
        verify(exactly = 1) { service.getContracts(any()) }
    }

}
