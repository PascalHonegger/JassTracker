package dev.honegger.services

import dev.honegger.domain.Round
import dev.honegger.domain.UserSession
import dev.honegger.repositories.RoundRepository
import dev.honegger.repositories.TableRepository

import mu.KotlinLogging
import java.util.*

interface RoundService {
    fun createRound(
        session: UserSession,
        number: Int,
        score: Int,
        gameId: UUID,
        playerId: UUID,
        contractId: UUID
    ): Round

    fun getRounds(session: UserSession, gameId: UUID): List<Round>
    fun updateRound(session: UserSession, updatedRound: Round)
}

private val log = KotlinLogging.logger { }

class RoundServiceImpl(private val roundRepository: RoundRepository, private val tableRepository: TableRepository) : RoundService {
    override fun createRound(
        session: UserSession,
        number: Int,
        score: Int,
        gameId: UUID,
        playerId: UUID,
        contractId: UUID,
    ): Round {
        check(score in 0..157) { "Score must be between 0 and 157" }

        val table = tableRepository.getTableByGameIdOrNull(gameId) ?: error("Couldn't find gameId")
        val game = table.games.single()

        // Santity checks:
        // - contract is valid and not done by other team member
        // - playerId is part of game
        // - ...

        val newRound = Round(
            id = UUID.randomUUID(),
            number = number,
            score = score,
            gameId = gameId,
            playerId = playerId,
            contractId = contractId,
        )

        // TODO verify round is part of game / table which is owned by current user

        log.info { "Saving new round $newRound" }
        roundRepository.saveRound(newRound)
        return newRound
    }

    override fun getRounds(
        session: UserSession,
        gameId: UUID,
    ): List<Round> {
        return roundRepository.getRoundsForGame(gameId)
    }

    override fun updateRound(
        session: UserSession,
        updatedRound: Round,
    ) {
        check(updatedRound.score in 0..157) { "Score must be between 0 and 157" }
        val existingRound =
            roundRepository.getRoundOrNull(updatedRound.id)
        // User can only update a round which exists
        checkNotNull(existingRound)

        // TODO verify round is part of game / table which is owned by current user

        roundRepository.updateRound(existingRound)
    }
}