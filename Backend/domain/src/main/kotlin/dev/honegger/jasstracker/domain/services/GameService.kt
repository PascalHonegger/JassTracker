package dev.honegger.jasstracker.domain.services

import dev.honegger.jasstracker.domain.*
import dev.honegger.jasstracker.domain.repositories.GameRepository
import dev.honegger.jasstracker.domain.repositories.PlayerRepository
import dev.honegger.jasstracker.domain.repositories.TableRepository
import dev.honegger.jasstracker.domain.util.displayNameLengthRange
import dev.honegger.jasstracker.domain.util.validateCurrentPlayer
import dev.honegger.jasstracker.domain.util.validateExists
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import mu.KotlinLogging
import java.util.*

interface GameService {
    fun createGame(
        session: PlayerSession,
        tableId: UUID,
        team1Player1: CreateGameParticipation,
        team1Player2: CreateGameParticipation,
        team2Player1: CreateGameParticipation,
        team2Player2: CreateGameParticipation,
    ): Game

    fun getGame(session: PlayerSession, id: UUID): Game
    fun getAllGames(session: PlayerSession): List<Game>
    fun updateGame(session: PlayerSession, updatedGame: Game)
    fun deleteGameById(session: PlayerSession, id: UUID)
}

data class CreateGameParticipation(
    val playerId: UUID?,
    val displayName: String,
)

private val log = KotlinLogging.logger { }

class GameServiceImpl(
    private val gameRepository: GameRepository,
    private val tableRepository: TableRepository,
    private val playerRepository: PlayerRepository,
    private val clock: Clock = Clock.System,
) :
    GameService {
    override fun createGame(
        session: PlayerSession,
        tableId: UUID,
        team1Player1: CreateGameParticipation,
        team1Player2: CreateGameParticipation,
        team2Player1: CreateGameParticipation,
        team2Player2: CreateGameParticipation,
    ): Game {
        val playerIds = listOfNotNull(
            team1Player1.playerId,
            team1Player2.playerId,
            team2Player1.playerId,
            team2Player2.playerId
        )
        require(playerIds.size == playerIds.toSet().size) { "Players in a game must be unique" }
        val table = tableRepository.getTableOrNull(tableId)
        validateExists(table) { "Table $tableId does not exist" }
        validateCurrentPlayer(table.ownerId, session) { "Only table owner can create games" }
        val newGame = Game(
            id = UUID.randomUUID(),
            startTime = clock.now().toLocalDateTime(TimeZone.UTC),
            rounds = emptyList(),
            team1 = Team(createParticipation(team1Player1), createParticipation(team1Player2)),
            team2 = Team(createParticipation(team2Player1), createParticipation(team2Player2)),
        )
        log.info { "Saving new game $newGame for table $tableId" }
        gameRepository.saveGame(newGame, tableId)
        return newGame
    }

    override fun getGame(
        session: PlayerSession,
        id: UUID,
    ): Game {
        // Users can load any scoreboard they know the ID of
        val game = gameRepository.getGameOrNull(id)
        validateExists(game) { "Game $id was not found" }
        return game
    }

    override fun getAllGames(session: PlayerSession): List<Game> {
        // Users can load all games, will be restricted in the future
        return gameRepository.getAllGames()
    }

    override fun updateGame(
        session: PlayerSession,
        updatedGame: Game,
    ) {
        val existingGame = gameRepository.getGameOrNull(updatedGame.id)
        validateExists(existingGame) { "Game ${updatedGame.id} was not found and cannot be updated" }
        val table = tableRepository.getTableByGameIdOrNull(existingGame.id)
        checkNotNull(table)
        validateCurrentPlayer(table.ownerId, session) { "Only table owner can update games" }
        require(updatedGame.endTime == null || updatedGame.endTime >= updatedGame.startTime)
        gameRepository.updateGame(existingGame.copy(endTime = updatedGame.endTime))
    }

    private fun createParticipation(createGameParticipation: CreateGameParticipation): GameParticipation {
        require(createGameParticipation.displayName.length in displayNameLengthRange) { "Name must be between $displayNameLengthRange characters" }
        if (createGameParticipation.playerId != null) {
            return GameParticipation(createGameParticipation.playerId, createGameParticipation.displayName)
        }

        val newGuest = GuestPlayer(
            id = UUID.randomUUID(),
        )
        playerRepository.savePlayer(newGuest)
        return GameParticipation(newGuest.id, createGameParticipation.displayName)
    }

    override fun deleteGameById(session: PlayerSession, id: UUID) {
        val table = tableRepository.getTableByGameIdOrNull(id)
        validateExists(table) { "Table for game $id does not exist" }
        validateCurrentPlayer(table.ownerId, session) { "Only table owner can delete games" }
        val wasDeleted = gameRepository.deleteGameById(id)
        if (!wasDeleted) {
            log.warn { "Delete game $id did not result in DB update" }
        }
    }
}
