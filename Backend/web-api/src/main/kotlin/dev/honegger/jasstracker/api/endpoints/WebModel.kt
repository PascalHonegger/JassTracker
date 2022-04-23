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

// Would be loaded once authentication is implemented
val dummySession = UserSession(userId = UUID.fromString("27fa77f3-eb56-46a0-8ada-b0a6f2e26cc0"), "dummy")

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
)

@Serializable
data class WebTeam(
    val player1: WebGameParticipant,
    val player2: WebGameParticipant,
)

@Serializable
data class WebGameParticipant(
    @Serializable(with = UUIDSerializer::class)
    val playerId: UUID,
    val displayName: String,
)

@Serializable
data class WebCreateGameParticipant(
    @Serializable(with = UUIDSerializer::class)
    val playerId: UUID?,
    val displayName: String,
)

@Serializable
data class WebCreateGame(
    val tableId: String,
    val team1Player1: WebCreateGameParticipant,
    val team1Player2: WebCreateGameParticipant,
    val team2Player1: WebCreateGameParticipant,
    val team2Player2: WebCreateGameParticipant,
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
    latestGame = games.maxByOrNull { it.startTime }?.toWebGame(),
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
)

fun WebTeam.toTeam() = Team(
    player1 = GameParticipant(player1.playerId, player1.displayName),
    player2 = GameParticipant(player2.playerId, player2.displayName),
)
fun Team.toWebTeam() = WebTeam(
    player1 = WebGameParticipant(player1.playerId, player1.displayName),
    player2 = WebGameParticipant(player2.playerId, player2.displayName),
)

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
