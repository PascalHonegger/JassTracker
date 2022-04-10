package dev.honegger.services

import dev.honegger.domain.Contract
import dev.honegger.domain.UserSession
import dev.honegger.repositories.ContractRepository

interface ContractService {
    fun getContracts(session: UserSession): List<Contract>
}

class ContractServiceImpl(private val contractRepository: ContractRepository) : ContractService {
    override fun getContracts(session: UserSession): List<Contract> {
        return contractRepository.getContracts()
    }
}
