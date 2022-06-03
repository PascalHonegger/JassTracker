package dev.honegger.jasstracker.domain.services

import dev.honegger.jasstracker.domain.Game
import dev.honegger.jasstracker.domain.Round
import dev.honegger.jasstracker.domain.PlayerSession
import dev.honegger.jasstracker.domain.repositories.ContractRepository
import dev.honegger.jasstracker.domain.repositories.RoundRepository
import dev.honegger.jasstracker.domain.repositories.TableRepository
import dev.honegger.jasstracker.domain.util.*

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
    private fun validateScore(score: Int): Unit = require(score in scoreRange) { "Score must be between $scoreRange" }

    override fun createRound(
        session: PlayerSession,
        number: Int,
        score: Int,
        gameId: UUID,
        playerId: UUID,
        contractId: UUID,
    ): Round {
        validateScore(score)
        require(number in roundNumberRange) { "Number must be between $roundNumberRange" }

        require(contractRepository.contractExists(contractId)) { "Contract $contractId does not exist" }

        val table = tableRepository.getTableByGameIdOrNull(gameId)
        validateExists(table) { "Game $gameId does not exist" }
        validateCurrentPlayer(table.ownerId, session) { "Only table owner can add new rounds to game" }

        val game = table.games.single { it.id == gameId }
        require(game.endTime == null) { "Cannot add round to completed game" }
        val team = getTeamOfPlayer(game, playerId)

        val expectedRoundNumber = (game.rounds.maxOfOrNull { it.number } ?: 0) + 1
        require(number == expectedRoundNumber) { "Expected round number $expectedRoundNumber but got $number" }
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
        validateScore(updatedRound.score)
        val existingRound = roundRepository.getRoundOrNull(updatedRound.id)
        validateExists(existingRound) { "Player can only update a round which exists" }
        validateTable(session, existingRound, "update")
        roundRepository.updateRound(existingRound.copy(score = updatedRound.score))
    }

    override fun deleteRoundById(session: PlayerSession, id: UUID): Boolean {
        val existingRound = roundRepository.getRoundOrNull(id) ?: return false
        validateTable(session, existingRound, "delete")
        return roundRepository.deleteRoundById(id)
    }

    private fun validateTable(session: PlayerSession, round: Round, action: String) {
        val table = tableRepository.getTableByGameIdOrNull(round.gameId)
        checkNotNull(table)
        validateCurrentPlayer(table.ownerId, session) { "Player can only $action round on owned table" }
        val game = table.games.single { it.id == round.gameId }
        require(game.endTime == null) { "Player cannot $action round of completed game" }
    }
}
