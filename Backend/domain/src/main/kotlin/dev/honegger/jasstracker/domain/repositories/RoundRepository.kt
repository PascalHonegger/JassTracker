package dev.honegger.jasstracker.domain.repositories

import dev.honegger.jasstracker.domain.Round
import java.util.*

interface RoundRepository {
    fun getRoundOrNull(id: UUID): Round?
    fun updateRound(updatedRound: Round)
    fun saveRound(newRound: Round)
    fun deleteRoundById(id: UUID): Boolean
}
