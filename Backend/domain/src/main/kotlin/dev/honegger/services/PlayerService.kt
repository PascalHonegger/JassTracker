package dev.honegger.services

import dev.honegger.domain.Player
import dev.honegger.domain.UserSession
import dev.honegger.repositories.PlayerRepository
import mu.KotlinLogging
import java.util.*

interface PlayerService {
    fun createPlayer(
        session: UserSession,
        displayName: String,
        username: String,
        password: String,
    ): Player

    fun getPlayerOrNull(session: UserSession, id: UUID): Player?
    fun getPlayersPerTable(session: UserSession, tableId: UUID): List<Player>
    fun updatePlayer(session: UserSession, updatedPlayer: Player)
}

private val log = KotlinLogging.logger { }

class PlayerServiceImpl(private val playerRepository: PlayerRepository) : PlayerService {
    override fun createPlayer(
        session: UserSession,
        displayName: String,
        username: String,
        password: String,
    ): Player {
        val newPlayer = Player(
            id = UUID.randomUUID(),
            displayName = displayName,
            username = username,
            password = password, // TODO Hashing & Security once we look at authentication
            isGuest = false,
        )

        log.info { "Saving new player $newPlayer" }
        playerRepository.savePlayer(newPlayer)
        return newPlayer
    }

    override fun getPlayerOrNull(
        session: UserSession,
        id: UUID,
    ): Player? {
        // Users can load any player they know the ID of
        return playerRepository.getPlayerOrNull(id)
    }

    override fun getPlayersPerTable(session: UserSession, tableId: UUID): List<Player> {
        return playerRepository.getPlayersPerTable(tableId)
    }

    override fun updatePlayer(
        session: UserSession,
        updatedPlayer: Player,
    ) {
        val existingPlayer =
            playerRepository.getPlayerOrNull(updatedPlayer.id)
        // User can only update themselves
        checkNotNull(existingPlayer)
        check(existingPlayer.id == session.userId)
        playerRepository.updatePlayer(existingPlayer.copy(displayName = existingPlayer.displayName))
    }
}
