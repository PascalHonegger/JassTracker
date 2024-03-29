package dev.honegger.jasstracker.domain.services

import dev.honegger.jasstracker.domain.GuestPlayer
import dev.honegger.jasstracker.domain.Player
import dev.honegger.jasstracker.domain.RegisteredPlayer
import dev.honegger.jasstracker.domain.PlayerSession
import dev.honegger.jasstracker.domain.repositories.PlayerRepository
import dev.honegger.jasstracker.domain.util.validateCurrentPlayer
import dev.honegger.jasstracker.domain.util.validateExists
import io.github.oshai.kotlinlogging.KotlinLogging
import java.util.*

interface PlayerService {
    fun registerPlayer(
        displayName: String,
        username: String,
        password: String,
    ): AuthToken

    fun registerGuestPlayer(): AuthToken
    fun authenticatePlayer(username: String, password: String): AuthToken
    fun getPlayer(session: PlayerSession, id: UUID): Player
    fun updatePlayerDisplayName(session: PlayerSession, updatedDisplayName: String): AuthToken
    fun updatePlayerPassword(session: PlayerSession, oldPassword: String, newPassword: String): AuthToken
    fun deletePlayer(session: PlayerSession, playerId: UUID)
}

private val log = KotlinLogging.logger { }

class PlayerServiceImpl(
    private val playerRepository: PlayerRepository,
    private val passwordHashService: PasswordHashService,
    private val authTokenService: AuthTokenService,
) : PlayerService {
    override fun registerPlayer(
        displayName: String,
        username: String,
        password: String,
    ): AuthToken {
        val newPlayer = RegisteredPlayer(
            id = UUID.randomUUID(),
            displayName = displayName,
            username = username,
            password = passwordHashService.hashPassword(password),
        )

        log.info { "Registering new player: $newPlayer" }
        playerRepository.savePlayer(newPlayer)
        return authTokenService.createToken(newPlayer)
    }

    override fun registerGuestPlayer(): AuthToken {
        val newPlayer = GuestPlayer(
            id = UUID.randomUUID(),
        )

        log.info { "Registering new guest: $newPlayer" }
        playerRepository.savePlayer(newPlayer)
        return authTokenService.createToken(newPlayer)
    }

    override fun authenticatePlayer(username: String, password: String): AuthToken {
        val player = playerRepository.findPlayerByUsername(username)
        require(
            player != null && passwordHashService.verifyPassword(
                hash = player.password,
                password = password
            )
        ) { "Incorrect password or username supplied" }
        return authTokenService.createToken(player)
    }

    override fun getPlayer(
        session: PlayerSession,
        id: UUID,
    ): Player {
        // Users can load any player they know the ID of
        val player = playerRepository.getPlayerOrNull(id)
        validateExists(player) { "Player $id not found" }
        return player
    }

    private fun updatePlayer(
        session: PlayerSession,
        updatedPlayer: RegisteredPlayer,
    ): AuthToken {
        // User can only update themselves
        check(updatedPlayer.id == session.playerId)
        playerRepository.updatePlayer(updatedPlayer)
        return authTokenService.createToken(updatedPlayer)
    }

    override fun updatePlayerDisplayName(
        session: PlayerSession,
        updatedDisplayName: String,
    ): AuthToken {
        val existingPlayer = playerRepository.getPlayerOrNull(session.playerId)
        require(existingPlayer is RegisteredPlayer) { "GuestPlayer cannot have display name" }

        val updatedPlayer = existingPlayer.copy(displayName = updatedDisplayName)

        return updatePlayer(session, updatedPlayer)
    }

    override fun updatePlayerPassword(session: PlayerSession, oldPassword: String, newPassword: String): AuthToken {
        val existingPlayer = playerRepository.getPlayerOrNull(session.playerId)
        require(existingPlayer is RegisteredPlayer) { "GuestPlayer cannot have password" }
        require(
            passwordHashService.verifyPassword(
                hash = existingPlayer.password,
                password = oldPassword
            )
        ) { "Incorrect Password supplied" }
        val updatedPlayer = existingPlayer.copy(password = passwordHashService.hashPassword(newPassword))
        return updatePlayer(session, updatedPlayer)
    }

    override fun deletePlayer(session: PlayerSession, playerId: UUID) {
        val existingPlayer = playerRepository.getPlayerOrNull(playerId)
        validateExists(existingPlayer) { "Player $playerId was not found" }
        validateCurrentPlayer(existingPlayer.id, session) { "Can only delete current user" }

        val updatedPlayer = GuestPlayer(existingPlayer.id)
        playerRepository.updatePlayer(updatedPlayer)
    }
}
