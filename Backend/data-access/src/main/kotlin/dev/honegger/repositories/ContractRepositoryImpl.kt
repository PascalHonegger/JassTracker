package dev.honegger.repositories

import dev.honegger.domain.Contract
import dev.honegger.jasstracker.database.enums.ContractType
import dev.honegger.jasstracker.database.tables.Contract.CONTRACT
import dev.honegger.jasstracker.database.tables.records.ContractRecord
import dev.honegger.withContext
import java.util.*

class ContractRepositoryImpl : ContractRepository {
    private fun ContractRecord.toContract() = Contract(
        id = id,
        name = name,
        multiplier = multiplier,
        type = when (type) {
            ContractType.acorns -> dev.honegger.domain.ContractType.Acorns
            ContractType.roses -> dev.honegger.domain.ContractType.Roses
            ContractType.shields -> dev.honegger.domain.ContractType.Shields
            ContractType.bells -> dev.honegger.domain.ContractType.Bells
            ContractType.tops_down -> dev.honegger.domain.ContractType.TopsDown
            ContractType.bottoms_up -> dev.honegger.domain.ContractType.BottomsUp
            ContractType.joker -> dev.honegger.domain.ContractType.Joker
            ContractType.slalom -> dev.honegger.domain.ContractType.Slalom
            ContractType.guschti -> dev.honegger.domain.ContractType.Guschti
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
