package dev.honegger.jasstracker.domain.services

import dev.honegger.jasstracker.domain.*
import dev.honegger.jasstracker.domain.repositories.GameRepository
import dev.honegger.jasstracker.domain.repositories.PlayerRepository
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
    fun getGameOrNull(session: PlayerSession, id: UUID): Game?
    fun getAllGames(session: PlayerSession): List<Game>
    fun updateGame(session: PlayerSession, updatedGame: Game)
    fun deleteGameById(session: PlayerSession, id: UUID): Boolean
}

data class CreateGameParticipation(
    val playerId: UUID?,
    val displayName: String,
)

private val log = KotlinLogging.logger { }

class GameServiceImpl(private val gameRepository: GameRepository, private val playerRepository: PlayerRepository, private val clock: Clock = Clock.System) :
    GameService {
    override fun createGame(
        session: PlayerSession,
        tableId: UUID,
        team1Player1: CreateGameParticipation,
        team1Player2: CreateGameParticipation,
        team2Player1: CreateGameParticipation,
        team2Player2: CreateGameParticipation,
    ): Game {
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

    override fun getGameOrNull(
        session: PlayerSession,
        id: UUID,
    ): Game? {
        // Users can load any scoreboard they know the ID of
        return gameRepository.getGameOrNull(id)
    }

    override fun getAllGames(session: PlayerSession): List<Game> {
        // Users can load all games, will be restricted in the future
        return gameRepository.getAllGames()
    }

    override fun updateGame(
        session: PlayerSession,
        updatedGame: Game,
    ) {
        val existingGame =
            gameRepository.getGameOrNull(updatedGame.id)
        // User can only update a scoreboard which exists and is owned by himself
        checkNotNull(existingGame)
        check(updatedGame.endTime == null || updatedGame.endTime >= updatedGame.startTime)
        gameRepository.updateGame(existingGame.copy(endTime = updatedGame.endTime))
    }

    private fun createParticipation(createGameParticipation: CreateGameParticipation): GameParticipation {
        if (createGameParticipation.playerId != null) {
            return GameParticipation(createGameParticipation.playerId, createGameParticipation.displayName)
        }

        val newGuest = GuestPlayer(
            id = UUID.randomUUID(),
        )
        playerRepository.savePlayer(newGuest)
        return GameParticipation(newGuest.id, createGameParticipation.displayName)
    }

    override fun deleteGameById(session: PlayerSession, id: UUID): Boolean {
        val existingGame =
            gameRepository.getGameOrNull(id)
        checkNotNull(existingGame)
        return gameRepository.deleteGameById(id)
    }
}
