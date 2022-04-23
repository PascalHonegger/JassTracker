package dev.honegger.jasstracker.data.repositories

import dev.honegger.jasstracker.data.database.enums.ContractType
import dev.honegger.jasstracker.data.database.tables.Contract.CONTRACT
import dev.honegger.jasstracker.data.database.tables.records.ContractRecord
import dev.honegger.jasstracker.domain.Contract
import dev.honegger.jasstracker.domain.repositories.ContractRepository
import dev.honegger.withContext
import java.util.*

class ContractRepositoryImpl : ContractRepository {
    private fun ContractRecord.toContract() = Contract(
        id = id,
        name = name,
        multiplier = multiplier,
        type = when (type) {
            ContractType.acorns -> dev.honegger.jasstracker.domain.ContractType.Acorns
            ContractType.roses -> dev.honegger.jasstracker.domain.ContractType.Roses
            ContractType.shields -> dev.honegger.jasstracker.domain.ContractType.Shields
            ContractType.bells -> dev.honegger.jasstracker.domain.ContractType.Bells
            ContractType.tops_down -> dev.honegger.jasstracker.domain.ContractType.TopsDown
            ContractType.bottoms_up -> dev.honegger.jasstracker.domain.ContractType.BottomsUp
            ContractType.joker -> dev.honegger.jasstracker.domain.ContractType.Joker
            ContractType.slalom -> dev.honegger.jasstracker.domain.ContractType.Slalom
            ContractType.guschti -> dev.honegger.jasstracker.domain.ContractType.Guschti
            null -> error("ContractType is not nullable in database")
        }
    )

    override fun getContracts(): List<Contract> = withContext {
        selectFrom(CONTRACT).fetch().map { it.toContract() }
    }

    override fun getContractOrNull(id: UUID): Contract? = withContext {
        selectFrom(CONTRACT).where(CONTRACT.ID.eq(id)).fetchOne()?.toContract()
    }
}
