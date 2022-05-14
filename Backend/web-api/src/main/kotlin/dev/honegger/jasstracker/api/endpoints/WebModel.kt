package dev.honegger.jasstracker.api.endpoints

import dev.honegger.jasstracker.domain.*
import dev.honegger.jasstracker.api.serializer.UUIDSerializer
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.Serializable
import java.util.*

// Keep in sync with web-model.ts !!

@Serializable
data class WebTable(
    @Serializable(with = UUIDSerializer::class)
    val id: UUID,
    val name: String,
    @Serializable(with = UUIDSerializer::class)
    val ownerId: UUID,
    val gameIds: List<@Serializable(with = UUIDSerializer::class) UUID>,
    val latestGame: WebGame?,
)

@Serializable
data class WebCreateTable(val name: String)

@Serializable
data class WebGame(
    @Serializable(with = UUIDSerializer::class)
    val id: UUID,
    val startTime: Instant,
    val endTime: Instant?,
    val rounds: List<WebRound>,
    val team1: WebTeam,
    val team2: WebTeam,
    val currentPlayer: WebGameParticipation,
)

@Serializable
data class WebTeam(
    val player1: WebGameParticipation,
    val player2: WebGameParticipation,
)

@Serializable
data class WebGameParticipation(
    @Serializable(with = UUIDSerializer::class)
    val playerId: UUID,
    val displayName: String,
)

@Serializable
data class WebCreateGameParticipation(
    @Serializable(with = UUIDSerializer::class)
    val playerId: UUID?,
    val displayName: String,
)

@Serializable
data class WebCreateGame(
    val tableId: String,
    val team1Player1: WebCreateGameParticipation,
    val team1Player2: WebCreateGameParticipation,
    val team2Player1: WebCreateGameParticipation,
    val team2Player2: WebCreateGameParticipation,
)

@Serializable
data class WebRound(
    @Serializable(with = UUIDSerializer::class)
    val id: UUID,
    val number: Int,
    val score: Int,
    @Serializable(with = UUIDSerializer::class)
    val gameId: UUID,
    @Serializable(with = UUIDSerializer::class)
    val playerId: UUID,
    @Serializable(with = UUIDSerializer::class)
    val contractId: UUID,
)

@Serializable
data class WebCreateRound(
    val number: Int,
    val score: Int,
    @Serializable(with = UUIDSerializer::class)
    val gameId: UUID,
    @Serializable(with = UUIDSerializer::class)
    val playerId: UUID,
    @Serializable(with = UUIDSerializer::class)
    val contractId: UUID
)

@Serializable
data class WebContract(
    @Serializable(with = UUIDSerializer::class)
    val id: UUID,
    val name: String,
    val multiplier: Int,
    val type: ContractType,
)

@Serializable
data class WebPlayer(
    @Serializable(with = UUIDSerializer::class)
    val id: UUID,
    val username: String?,
    val displayName: String?,
    val password: String?,
    val isGuest: Boolean
)

@Serializable
data class WebCreatePlayer(
    val username: String,
    val displayName: String,
    val password: String,
)

fun WebTable.toTable() = Table(
    id = id,
    name = name,
    ownerId = ownerId,
    games = emptyList(),
)

fun Table.toWebTable() = WebTable(
    id = id,
    name = name,
    ownerId = ownerId,
    gameIds = games.map { it.id },
    latestGame = latestGame?.toWebGame()
)

fun WebGame.toGame() = Game(
    id = id,
    startTime = startTime.toLocalDateTime(TimeZone.UTC),
    endTime = endTime?.toLocalDateTime(TimeZone.UTC),
    rounds = rounds.map { it.toRound() },
    team1 = team1.toTeam(),
    team2 = team2.toTeam(),
)
fun Game.toWebGame() = WebGame(
    id = id,
    startTime = startTime.toInstant(TimeZone.UTC),
    endTime = endTime?.toInstant(TimeZone.UTC),
    rounds = rounds.map { it.toWebRound() },
    team1 = team1.toWebTeam(),
    team2 = team2.toWebTeam(),
    currentPlayer = currentPlayer.toWebGameParticipation()
)

fun WebTeam.toTeam() = Team(
    player1 = player1.toGameParticipation(),
    player2 = player2.toGameParticipation(),
)
fun Team.toWebTeam() = WebTeam(
    player1 = player1.toWebGameParticipation(),
    player2 = player2.toWebGameParticipation(),
)

fun WebGameParticipation.toGameParticipation() = GameParticipation(playerId, displayName)
fun GameParticipation.toWebGameParticipation() = WebGameParticipation(playerId, displayName)

fun Contract.toWebContract() = WebContract(
    id = id,
    name = name,
    multiplier = multiplier,
    type = type,
)

fun WebRound.toRound() = Round(
    id = id,
    number = number,
    score = score,
    gameId = gameId,
    playerId = playerId,
    contractId = contractId,
)

fun Round.toWebRound() = WebRound(
    id = id,
    number = number,
    score = score,
    gameId = gameId,
    playerId = playerId,
    contractId = contractId,
)

fun WebPlayer.toPlayer() = when {
    isGuest -> GuestPlayer(id)
    else -> RegisteredPlayer(
        id = id,
        username = checkNotNull(username),
        displayName = checkNotNull(displayName),
        password = checkNotNull(password),
    )
}
fun Player.toWebPlayer() = when(this) {
    is GuestPlayer -> WebPlayer(
        id = id,
        username = null,
        displayName = null,
        password = null,
        isGuest = true,
    )
    is RegisteredPlayer -> WebPlayer(
        id = id,
        username = username,
        displayName = displayName,
        password = null, // Do not return password to client
        isGuest = false,
    )
}
