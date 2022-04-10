package dev.honegger.repositories

import dev.honegger.domain.Contract
import java.util.*

interface ContractRepository {
    fun getContracts(): List<Contract>
    fun getContractOrNull(id: UUID): Contract?
}
