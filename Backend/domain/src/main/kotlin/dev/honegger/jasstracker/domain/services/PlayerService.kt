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
    ): RegisteredPlayer

    fun registerGuestPlayer(): GuestPlayer
    fun getPlayerOrNull(session: PlayerSession, id: UUID): Player?
    fun updatePlayer(session: PlayerSession, updatedPlayer: RegisteredPlayer)
    fun authenticatePlayer(username: String, password: String): RegisteredPlayer?
}

private val log = KotlinLogging.logger { }

class PlayerServiceImpl(
    private val playerRepository: PlayerRepository,
    private val passwordHashService: PasswordHashService,
) : PlayerService {
    override fun registerPlayer(
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

        log.info { "Registering new player: $newPlayer" }
        playerRepository.savePlayer(newPlayer)
        return newPlayer
    }

    override fun registerGuestPlayer(): GuestPlayer {
        val newPlayer = GuestPlayer(
            id = UUID.randomUUID(),
        )

        log.info { "Registering new guest: $newPlayer" }
        playerRepository.savePlayer(newPlayer)
        return newPlayer
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
        check(existingPlayer.id == session.userId)

        val sanitizedPlayer = when (existingPlayer) {
            is GuestPlayer -> updatedPlayer
            is RegisteredPlayer -> existingPlayer.copy(displayName = updatedPlayer.displayName)
        }
        playerRepository.updatePlayer(sanitizedPlayer)
    }

    override fun authenticatePlayer(username: String, password: String): RegisteredPlayer? {
        return playerRepository.findPlayerByUsername(username)?.takeIf {
            passwordHashService.verifyPassword(
                hash = it.password,
                password = password
            )
        }
    }
}
