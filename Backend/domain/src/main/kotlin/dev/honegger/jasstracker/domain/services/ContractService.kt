package dev.honegger.jasstracker.domain.services

import dev.honegger.jasstracker.domain.Contract
import dev.honegger.jasstracker.domain.UserSession
import dev.honegger.jasstracker.domain.repositories.ContractRepository

interface ContractService {
    fun getContracts(session: UserSession): List<Contract>
}

class ContractServiceImpl(private val contractRepository: ContractRepository) : ContractService {
    override fun getContracts(session: UserSession): List<Contract> {
        return contractRepository.getContracts()
    }
}
