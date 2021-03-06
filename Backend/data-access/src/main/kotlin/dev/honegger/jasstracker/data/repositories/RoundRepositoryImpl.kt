package dev.honegger.jasstracker.data.repositories

import dev.honegger.jasstracker.data.database.tables.Round.ROUND
import dev.honegger.jasstracker.data.database.tables.records.RoundRecord
import dev.honegger.jasstracker.data.withContext
import dev.honegger.jasstracker.domain.Round
import dev.honegger.jasstracker.domain.repositories.RoundRepository
import java.util.*

class RoundRepositoryImpl : RoundRepository {
    private fun RoundRecord.toRound() = Round(
        id = id,
        number = number,
        score = score,
        gameId = gameId,
        playerId = playerId,
        contractId = contractId,
    )
    override fun getRoundOrNull(id: UUID): Round? = withContext {
        selectFrom(ROUND).where(ROUND.ID.eq(id)).fetchOne()?.toRound()
    }

    override fun getAllRoundsByPlayer(playerId: UUID): List<Round> = withContext{
        selectFrom(ROUND).where(ROUND.PLAYER_ID.eq(playerId)).fetch().map { it.toRound() }
    }

    override fun updateRound(updatedRound: Round): Unit = withContext {
        val roundRecord = selectFrom(ROUND).where(ROUND.ID.eq(updatedRound.id)).fetchOne()
        checkNotNull(roundRecord)

        roundRecord.number = updatedRound.number
        roundRecord.score = updatedRound.score
        roundRecord.gameId = updatedRound.gameId
        roundRecord.playerId = updatedRound.playerId
        roundRecord.contractId = updatedRound.contractId
        roundRecord.update()
    }

    override fun saveRound(newRound: Round): Unit = withContext {
        val newRecord = newRecord(ROUND).apply {
            this.id = newRound.id
            this.number = newRound.number
            this.score = newRound.score
            this.gameId = newRound.gameId
            this.playerId = newRound.playerId
            this.contractId = newRound.contractId
        }
        newRecord.insert()
    }

    override fun deleteRoundById(id: UUID): Boolean = withContext {
        val round = selectFrom(ROUND).where(ROUND.ID.eq(id)).fetchOne() ?: return@withContext false
        update(ROUND)
            .set(ROUND.NUMBER, ROUND.NUMBER.minus(1))
            .where(ROUND.GAME_ID.eq(round.gameId), ROUND.NUMBER.gt(round.number))
            .execute()
        return@withContext deleteFrom(ROUND).where(ROUND.ID.eq(id)).execute() == 1
    }
}
