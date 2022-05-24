package dev.honegger.jasstracker.data.repositories

import dev.honegger.jasstracker.domain.GuestPlayer
import dev.honegger.jasstracker.domain.Player
import dev.honegger.jasstracker.domain.RegisteredPlayer
import dev.honegger.jasstracker.data.database.tables.Game.GAME
import dev.honegger.jasstracker.data.database.tables.Player.PLAYER
import dev.honegger.jasstracker.domain.repositories.PlayerRepository
import dev.honegger.jasstracker.data.database.tables.GameParticipation.GAME_PARTICIPATION as GP
import dev.honegger.jasstracker.data.withContext
import java.util.*

class PlayerRepositoryImpl : PlayerRepository {
    override fun getPlayersPerTable(tableId: UUID): List<Player> = withContext {
        val playerIds = select(GP.PLAYER_ID).from(GP).join(GAME)
            .on(GAME.ID.eq(GP.GAME_ID)).where(GAME.TABLE_ID.eq(tableId)).fetch()

        selectFrom(PLAYER).where(PLAYER.ID.`in`(playerIds)).fetch()
            .map {
                when {
                    it.isGuest -> GuestPlayer(it.id)
                    else -> RegisteredPlayer(
                        id = it.id,
                        username = it.username,
                        displayName = it.displayName,
                        password = it.password,
                    )
                }
            }
    }

    override fun getPlayerOrNull(id: UUID): Player? = withContext {
        val playerRecord = selectFrom(PLAYER).where(PLAYER.ID.eq(id)).fetchOne()

        playerRecord?.let {
            when {
                it.isGuest -> GuestPlayer(it.id)
                else -> RegisteredPlayer(
                    id = it.id,
                    username = it.username,
                    displayName = it.displayName,
                    password = it.password,
                )
            }
        }
    }

    override fun updatePlayer(updatedPlayer: Player): Unit = withContext {
        val playerRecord = selectFrom(PLAYER).where(PLAYER.ID.eq(updatedPlayer.id)).fetchOne()
        checkNotNull(playerRecord)

        when (updatedPlayer) {
            is GuestPlayer -> {
                playerRecord.apply {
                    username = null
                    password = null
                    displayName = null
                }
            }
            is RegisteredPlayer -> {
                playerRecord.apply {
                    username = updatedPlayer.username
                    password = updatedPlayer.password
                    displayName = updatedPlayer.displayName
                }
            }
        }
        playerRecord.update()
    }

    override fun updatePlayerDisplayName(id: UUID, updatedDisplayName: String): Unit = withContext {
        val playerRecord = selectFrom(PLAYER).where(PLAYER.ID.eq(id)).fetchOne()
        checkNotNull(playerRecord)
        playerRecord.displayName = updatedDisplayName
        playerRecord.update()
    }

    override fun savePlayer(newPlayer: Player): Unit = withContext {
        val newRecord = newRecord(PLAYER).apply {
            this.id = newPlayer.id
            when (newPlayer) {
                is GuestPlayer -> Unit
                is RegisteredPlayer -> {
                    this.username = newPlayer.username
                    this.displayName = newPlayer.displayName
                    this.password = newPlayer.password
                }
            }
        }
        newRecord.insert()
    }

    override fun findPlayerByUsername(username: String): RegisteredPlayer? = withContext {
        val playerRecord = selectFrom(PLAYER).where(PLAYER.USERNAME.eq(username)).fetchOne()

        playerRecord?.let {
            when {
                it.isGuest -> error("Cannot find guest by username")
                else -> RegisteredPlayer(
                    id = it.id,
                    username = it.username,
                    displayName = it.displayName,
                    password = it.password,
                )
            }
        }
    }
}
