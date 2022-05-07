package dev.honegger.jasstracker.domain.services

import dev.honegger.jasstracker.domain.GuestPlayer
import dev.honegger.jasstracker.domain.Player
import dev.honegger.jasstracker.domain.RegisteredPlayer
import dev.honegger.jasstracker.domain.UserSession
import dev.honegger.jasstracker.domain.repositories.PlayerRepository
import dev.honegger.jasstracker.security.Argon2PasswordHashService
import mu.KotlinLogging
import java.util.*

interface PlayerService {
    fun createPlayer(
        session: UserSession,
        displayName: String,
        username: String,
        password: String,
    ): RegisteredPlayer

    fun getPlayerOrNull(session: UserSession, id: UUID): Player?
    fun updatePlayer(session: UserSession, updatedPlayer: RegisteredPlayer)
}

private val log = KotlinLogging.logger { }

class PlayerServiceImpl(private val playerRepository: PlayerRepository, private val passwordHashService: Argon2PasswordHashService) : PlayerService {
    override fun createPlayer(
        session: UserSession,
        displayName: String,
        username: String,
        password: String,
    ): RegisteredPlayer {
        val newPlayer = RegisteredPlayer(
            id = UUID.randomUUID(),
            displayName = displayName,
            username = username,
            password = passwordHashService.hashPassword(password).toString(),
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

    override fun updatePlayer(
        session: UserSession,
        updatedPlayer: RegisteredPlayer,
    ) {
        val existingPlayer =
            playerRepository.getPlayerOrNull(updatedPlayer.id)
        // User can only update themselves
        checkNotNull(existingPlayer)
        check(existingPlayer.id == session.userId)

        val sanitizedPlayer = when (existingPlayer) {
            is GuestPlayer -> updatedPlayer
            is RegisteredPlayer -> existingPlayer.copy(displayName = updatedPlayer.displayName)
        }
        playerRepository.updatePlayer(sanitizedPlayer)
    }
}
