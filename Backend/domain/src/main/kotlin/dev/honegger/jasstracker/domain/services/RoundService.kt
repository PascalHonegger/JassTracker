package dev.honegger.jasstracker.domain.services

import dev.honegger.jasstracker.domain.Game
import dev.honegger.jasstracker.domain.Round
import dev.honegger.jasstracker.domain.PlayerSession
import dev.honegger.jasstracker.domain.repositories.ContractRepository
import dev.honegger.jasstracker.domain.repositories.RoundRepository
import dev.honegger.jasstracker.domain.repositories.TableRepository
import dev.honegger.jasstracker.domain.util.validateCurrentPlayer
import dev.honegger.jasstracker.domain.util.validateExists

import mu.KotlinLogging
import java.util.*

interface RoundService {
    fun createRound(
        session: PlayerSession,
        number: Int,
        score: Int,
        gameId: UUID,
        playerId: UUID,
        contractId: UUID
    ): Round
    fun updateRound(session: PlayerSession, updatedRound: Round)
    fun deleteRoundById(session: PlayerSession, id: UUID): Boolean
}

private val log = KotlinLogging.logger { }

class RoundServiceImpl(private val roundRepository: RoundRepository, private val tableRepository: TableRepository, private val contractRepository: ContractRepository) :
    RoundService {
    override fun createRound(
        session: PlayerSession,
        number: Int,
        score: Int,
        gameId: UUID,
        playerId: UUID,
        contractId: UUID,
    ): Round {
        require(score in 0..157) { "Score must be between 0 and 157" }
        require(number in 1..20) { "Number must be between 1 and 20" }

        require(contractRepository.contractExists(contractId)) { "Contract $contractId does not exist" }

        val table = tableRepository.getTableByGameIdOrNull(gameId)
        validateExists(table) { "Game $gameId does not exist" }
        validateCurrentPlayer(table.ownerId, session) { "Only table owner can add new rounds to game" }

        val game = table.games.single { it.id == gameId }
        val team = getTeamOfPlayer(game, playerId)

        require(game.rounds.none { it.number == number }) { "Round nr. $number was already played" }
        require(game.rounds.none { it.playerId in team && it.contractId == contractId }) { "Contract $contractId was already played by team member" }

        val newRound = Round(
            id = UUID.randomUUID(),
            number = number,
            score = score,
            gameId = gameId,
            playerId = playerId,
            contractId = contractId,
        )

        log.info { "Saving new round $newRound" }
        roundRepository.saveRound(newRound)
        return newRound
    }

    private fun getTeamOfPlayer(game: Game, playerId: UUID) = when (playerId) {
        in game.team1 -> game.team1
        in game.team2 -> game.team2
        else -> throw IllegalArgumentException("Player $playerId is not in game ${game.id}")
    }

    override fun updateRound(
        session: PlayerSession,
        updatedRound: Round,
    ) {
        check(updatedRound.score in 0..157) { "Score must be between 0 and 157" }
        val existingRound = roundRepository.getRoundOrNull(updatedRound.id)
        validateExists(existingRound) { "Player can only update a round which exists" }
        // TODO verify round is part of game / table which is owned by current user
        roundRepository.updateRound(existingRound.copy(score = updatedRound.score))
    }

    override fun deleteRoundById(session: PlayerSession, id: UUID): Boolean {
        val existingRound = roundRepository.getRoundOrNull(id) ?: return false
        // TODO verify round is part of game / table which is owned by current user
        return roundRepository.deleteRoundById(id)
    }
}
