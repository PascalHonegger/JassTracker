package dev.honegger.domain

import java.util.*

data class Contract(
    val id: UUID,
    val name: String,
    val multiplier: Int,
    val type: ContractType
)

enum class ContractType {
    Acorns,
    Roses,
    Shields,
    Bells,
    TopsDown,
    BottomsUp,
    Joker,
    Slalom,
    Guschti,
}
