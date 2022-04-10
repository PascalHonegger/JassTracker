package dev.honegger.repositories

import dev.honegger.domain.Round
import java.util.*

interface RoundRepository {
    fun getRoundOrNull(id: UUID): Round?
    fun getRoundsForGame(gameId: UUID): List<Round>
    fun updateRound(updatedRound: Round)
    fun saveRound(newRound: Round)
}
