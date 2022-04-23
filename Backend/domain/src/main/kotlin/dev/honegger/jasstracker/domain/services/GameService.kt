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
        session: UserSession,
        tableId: UUID,
        team1Player1: CreateGameParticipant,
        team1Player2: CreateGameParticipant,
        team2Player1: CreateGameParticipant,
        team2Player2: CreateGameParticipant,
    ): Game
    fun getGameOrNull(session: UserSession, id: UUID): Game?
    fun getAllGames(session: UserSession): List<Game>
    fun updateGame(session: UserSession, updatedGame: Game)
}

data class CreateGameParticipant(
    val playerId: UUID?,
    val displayName: String,
)

private val log = KotlinLogging.logger { }

class GameServiceImpl(private val gameRepository: GameRepository, private val playerRepository: PlayerRepository, private val clock: Clock = Clock.System) :
    GameService {
    override fun createGame(
        session: UserSession,
        tableId: UUID,
        team1Player1: CreateGameParticipant,
        team1Player2: CreateGameParticipant,
        team2Player1: CreateGameParticipant,
        team2Player2: CreateGameParticipant,
    ): Game {
        val newGame = Game(
            id = UUID.randomUUID(),
            startTime = clock.now().toLocalDateTime(TimeZone.UTC),
            rounds = emptyList(),
            team1 = Team(createParticipant(team1Player1), createParticipant(team1Player2)),
            team2 = Team(createParticipant(team2Player1), createParticipant(team2Player2)),
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

    private fun createParticipant(createGameParticipant: CreateGameParticipant): GameParticipant {
        if (createGameParticipant.playerId != null) {
            return GameParticipant(createGameParticipant.playerId, createGameParticipant.displayName)
        }

        val newGuest = GuestPlayer(
            id = UUID.randomUUID(),
        )
        playerRepository.savePlayer(newGuest)
        return GameParticipant(newGuest.id, createGameParticipant.displayName)
    }
}
