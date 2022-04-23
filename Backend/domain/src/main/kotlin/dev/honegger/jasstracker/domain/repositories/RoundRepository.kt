package dev.honegger.jasstracker.domain.repositories

import dev.honegger.jasstracker.domain.Round
import java.util.*

interface RoundRepository {
    fun getRoundOrNull(id: UUID): Round?
    fun getRoundsForGame(gameId: UUID): List<Round>
    fun updateRound(updatedRound: Round)
    fun saveRound(newRound: Round)
}
