package dev.honegger.services

import dev.honegger.domain.Game
import dev.honegger.domain.UserSession
import dev.honegger.repositories.GameRepository
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import mu.KotlinLogging
import java.util.*

interface GameService {
    fun createGame(session: UserSession, tableId: UUID): Game
    fun getGameOrNull(session: UserSession, id: UUID): Game?
    fun getAllGames(session: UserSession): List<Game>
    fun updateGame(session: UserSession, updatedGame: Game)
}

private val log = KotlinLogging.logger { }

class GameServiceImpl(private val gameRepository: GameRepository, private val clock: Clock = Clock.System) :
    GameService {
    override fun createGame(
        session: UserSession,
        tableId: UUID,
    ): Game {
        val newGame = Game(
            id = UUID.randomUUID(),
            startTime = clock.now().toLocalDateTime(TimeZone.UTC),
        )

        log.info { "Saving new game $newGame for table $tableId" }
        gameRepository.saveGame(newGame, tableId)
        return newGame
    }

    override fun getGameOrNull(
        session: UserSession,
        id: UUID,
    ): Game? {
        // Users can load any scoreboard they know the ID of
        return gameRepository.getGameOrNull(id)
    }

    override fun getAllGames(session: UserSession): List<Game> {
        // Users can load all games, will be restricted in the future
        return gameRepository.getAllGames()
    }

    override fun updateGame(
        session: UserSession,
        updatedGame: Game,
    ) {
        val existingGame =
            gameRepository.getGameOrNull(updatedGame.id)
        // User can only update a scoreboard which exists and is owned by himself
        checkNotNull(existingGame)
        check(updatedGame.endTime == null || updatedGame.endTime >= updatedGame.startTime)
        gameRepository.updateGame(existingGame.copy(endTime = updatedGame.endTime))
    }
}
