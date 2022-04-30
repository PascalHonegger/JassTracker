package dev.honegger.jasstracker.data.repositories

import dev.honegger.jasstracker.domain.Contract
import dev.honegger.jasstracker.domain.ContractType
import java.util.*
import kotlin.test.Test
import kotlin.test.assertEquals

class ContractRepositoryImplTest : RepositoryTest() {
    private val repo = ContractRepositoryImpl()

    @Test
    fun `getContract returns contract`() {
        val id = UUID.fromString("58bae0f8-8c59-4a40-aa2d-9c6a489366b3")
        val expected = Contract(id, "Eicheln", 1, ContractType.Acorns)
        assertEquals(expected, repo.getContractOrNull(id))
    }
}