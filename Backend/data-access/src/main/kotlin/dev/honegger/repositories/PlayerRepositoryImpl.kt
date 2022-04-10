package dev.honegger.repositories

import dev.honegger.domain.Player
import dev.honegger.jasstracker.database.tables.Game.GAME
import dev.honegger.jasstracker.database.tables.Player.PLAYER
import dev.honegger.jasstracker.database.tables.GameParticipation.GAME_PARTICIPATION as GP
import dev.honegger.withContext
import java.util.*

class PlayerRepositoryImpl : PlayerRepository {
    override fun getPlayersPerTable(tableId: UUID): List<Player> = withContext {
        val playerIds = select(GP.PLAYER_ID).from(GP).join(GAME)
            .on(GAME.ID.eq(GP.GAME_ID)).where(GAME.TABLE_ID.eq(tableId)).fetch()

        selectFrom(PLAYER).where(PLAYER.ID.`in`(playerIds)).fetch()
            .map {
                Player(
                    id = it.id,
                    username = it.username,
                    displayName = it.displayName,
                    password = it.password,
                    isGuest = it.isGuest
                )
            }
    }

    override fun getPlayerOrNull(id: UUID): Player? = withContext {
        val playerRecord = selectFrom(PLAYER).where(PLAYER.ID.eq(id)).fetchOne()

        playerRecord?.let {
            Player(
                id = it.id,
                username = it.username,
                displayName = it.displayName,
                password = it.password,
                isGuest = it.isGuest,
            )
        }
    }

    override fun updatePlayer(updatedPlayer: Player): Unit = withContext {
        val playerRecord = selectFrom(PLAYER).where(PLAYER.ID.eq(updatedPlayer.id)).fetchOne()
        checkNotNull(playerRecord)

        playerRecord.displayName = updatedPlayer.displayName
        playerRecord.isGuest = updatedPlayer.isGuest
        playerRecord.store()
    }

    override fun savePlayer(newPlayer: Player): Unit = withContext {
        val newRecord = newRecord(PLAYER).apply {
            this.id = newPlayer.id
            this.username = newPlayer.username
            this.displayName = newPlayer.displayName
            this.password = newPlayer.password
            this.isGuest = newPlayer.isGuest
        }
        newRecord.store()
    }
}
