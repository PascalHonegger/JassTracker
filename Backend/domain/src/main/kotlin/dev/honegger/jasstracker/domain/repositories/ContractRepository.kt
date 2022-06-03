package dev.honegger.jasstracker.domain.repositories

import dev.honegger.jasstracker.domain.Contract
import java.util.*

interface ContractRepository {
    fun getContracts(): List<Contract>
    fun getContractOrNull(id: UUID): Contract?
    fun contractExists(id: UUID): Boolean
}
