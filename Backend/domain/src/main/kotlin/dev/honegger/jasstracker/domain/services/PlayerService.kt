package dev.honegger.jasstracker.domain.services

import dev.honegger.jasstracker.domain.GuestPlayer
import dev.honegger.jasstracker.domain.Player
import dev.honegger.jasstracker.domain.RegisteredPlayer
import dev.honegger.jasstracker.domain.PlayerSession
import dev.honegger.jasstracker.domain.repositories.PlayerRepository
import mu.KotlinLogging
import java.util.*

interface PlayerService {
    fun registerPlayer(
        displayName: String,
        username: String,
        password: String,
    ): AuthToken

    fun registerGuestPlayer(): AuthToken
    fun authenticatePlayer(username: String, password: String): AuthToken?
    fun getPlayerOrNull(session: PlayerSession, id: UUID): Player?
    fun updatePlayer(session: PlayerSession, updatedPlayer: RegisteredPlayer)
    fun updatePlayerDisplayName(session: PlayerSession, updatedDisplayName: String): AuthToken
    fun deletePlayer(session: PlayerSession, playerToDelete: RegisteredPlayer)
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

    override fun authenticatePlayer(username: String, password: String): AuthToken? {
        val player = playerRepository.findPlayerByUsername(username)
        if (player == null || !passwordHashService.verifyPassword(
                hash = player.password,
                password = password
            )
        ) return null
        return authTokenService.createToken(player)
    }

    override fun getPlayerOrNull(
        session: PlayerSession,
        id: UUID,
    ): Player? {
        // Users can load any player they know the ID of
        return playerRepository.getPlayerOrNull(id)
    }

    override fun updatePlayer(
        session: PlayerSession,
        updatedPlayer: RegisteredPlayer,
    ) {
        val existingPlayer =
            playerRepository.getPlayerOrNull(updatedPlayer.id)
        // User can only update themselves
        checkNotNull(existingPlayer)
        check(existingPlayer.id == session.playerId)

        val sanitizedPlayer = when (existingPlayer) {
            is GuestPlayer -> updatedPlayer
            is RegisteredPlayer -> existingPlayer.copy(displayName = updatedPlayer.displayName)
        }
        playerRepository.updatePlayer(sanitizedPlayer)
    }

    override fun  updatePlayerDisplayName(
        session: PlayerSession,
        updatedDisplayName: String,
    ): AuthToken {
        // do I rly still need all this?
        val existingPlayer =
            playerRepository.getPlayerOrNull(session.playerId)
        checkNotNull(existingPlayer)
        // User can only update themselves

        check(existingPlayer.id == session.playerId)

        playerRepository.updatePlayerDisplayName(session.playerId, updatedDisplayName)
        val updatedPlayer =
            playerRepository.getPlayerOrNull(session.playerId)
        checkNotNull(updatedPlayer)
        return authTokenService.createToken(updatedPlayer)
    }

    override fun deletePlayer(session: PlayerSession, playerToDelete: RegisteredPlayer) {
        val existingPlayer = playerRepository.getPlayerOrNull(playerToDelete.id)
        checkNotNull(existingPlayer)
        check(existingPlayer is RegisteredPlayer)
        check(existingPlayer.id == session.playerId)

        val updatedPlayer = GuestPlayer(existingPlayer.id)
        playerRepository.updatePlayer(updatedPlayer)
    }
}
