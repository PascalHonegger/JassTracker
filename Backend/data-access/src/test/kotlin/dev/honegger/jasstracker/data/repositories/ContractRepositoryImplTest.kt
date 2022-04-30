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

    @Test
    fun `getContracts returns all contracts`() {
        val expected = setOf(
            Contract(UUID.fromString("58bae0f8-8c59-4a40-aa2d-9c6a489366b3"), "Eicheln", 1, ContractType.Acorns),
            Contract(UUID.fromString("d895b400-3d89-48db-a7ed-5e593f54b7f6"), "Rosen", 2, ContractType.Roses),
            Contract(UUID.fromString("41c7bd00-3da4-4926-bcb6-08e12aafbe6d"), "Schilten", 3, ContractType.Shields),
            Contract(UUID.fromString("38fb8cbb-b22d-40f7-b9a1-b4adc1740075"), "Schellen", 4, ContractType.Bells),
            Contract(UUID.fromString("62aeb3b0-7b2d-4670-9789-6acd23fb8609"), "Obenabe", 5, ContractType.TopsDown),
            Contract(UUID.fromString("5a8de6ea-8da6-4c2b-b572-3d2335a7cbe2"), "Undenufe", 6, ContractType.BottomsUp),
            Contract(UUID.fromString("168b6602-07c3-4600-b39a-d08aca3323b0"), "Joker", 7, ContractType.Joker),
            Contract(UUID.fromString("02c40574-edd7-4a5b-baeb-e15cd50b1387"), "Joker", 8, ContractType.Joker),
            Contract(UUID.fromString("28d6e9ac-fc8e-4dad-8af8-2ae126b8d691"), "Slalom", 9, ContractType.Slalom),
            Contract(UUID.fromString("345bde8f-a316-4952-b021-7cbe7ad62306"), "Guschti", 10, ContractType.Guschti),
        )
        assertEquals(expected, repo.getContracts().toSet())
    }
}
