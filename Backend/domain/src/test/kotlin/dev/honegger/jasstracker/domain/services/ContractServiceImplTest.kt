package dev.honegger.jasstracker.domain.services

import dev.honegger.jasstracker.domain.Contract
import dev.honegger.jasstracker.domain.ContractType
import dev.honegger.jasstracker.domain.UserSession
import dev.honegger.jasstracker.domain.repositories.ContractRepository
import io.mockk.*
import java.util.*
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class ContractServiceImplTest {
    private val repository = mockk<ContractRepository>()
    private val dummySession = UserSession(UUID.randomUUID(), "dummy")
    private val service = ContractServiceImpl(repository)

    @BeforeTest
    fun setup() {
        clearMocks(repository)
    }

    @AfterTest
    fun teardown() {
        confirmVerified(repository)
    }

    @Test
    fun `getContracts returns all contracts in repository`() {
        val dummyAcorns = Contract(
            id = UUID.randomUUID(),
            name = "Eichle",
            multiplier = 1,
            type = ContractType.Acorns,
        )
        val dummyShields = Contract(
            id = UUID.randomUUID(),
            name = "Schilte",
            multiplier = 3,
            type = ContractType.Shields,
        )
        every { repository.getContracts() } returns listOf(
            dummyAcorns,
            dummyShields,
        )
        val loadedContracts = service.getContracts(dummySession)

        assertEquals(listOf(dummyAcorns, dummyShields), loadedContracts)

        verify(exactly = 1) { repository.getContracts() }
    }
}
