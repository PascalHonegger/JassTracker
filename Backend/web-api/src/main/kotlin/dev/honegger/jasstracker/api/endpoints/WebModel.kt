package dev.honegger.jasstracker.api.endpoints

import dev.honegger.jasstracker.api.serializer.UUIDSerializer
import dev.honegger.jasstracker.domain.*
import dev.honegger.jasstracker.domain.services.AuthToken
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
    val contractId: UUID,
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
    val isGuest: Boolean,
)

@Serializable
data class WebCreatePlayer(
    val username: String,
    val displayName: String,
    val password: String,
)

@Serializable
data class WebLogin(
    val username: String,
    val password: String,
)

@Serializable
data class TokenResponse(
    val token: String,
)

@Serializable
data class DisplayNameRequest(
    val displayName: String,
)

@Serializable
data class WebTeamScores(
    val team1Score: Int?,
    val team2Score: Int?,
)

@Serializable
data class WebGameScoreSummary(
    @Serializable(with = UUIDSerializer::class)
    val gameId: UUID,
    val total: WebTeamScores,
)

@Serializable
data class WebPlayerAverage(
    @Serializable(with = UUIDSerializer::class)
    val playerId: UUID,
    val displayName: String,
    val average: Double,
    val weightedAverage: Double,
)

@Serializable
data class WebGameStatistics(
    val playerAverages: List<WebPlayerAverage>,
    val team1Average: Double,
    val team2Average: Double,
    val team1WeightedAverage: Double,
    val team2WeightedAverage: Double,
    val expectedScoresOverTime: List<WebTeamScores>,
)

@Serializable
data class WebTableStatistics(
    val playerAverages: List<WebPlayerAverage>,
    val contractAverages: Map<@Serializable(with = UUIDSerializer::class) UUID, Double>,
    val scoresOverTime: List<WebGameScoreSummary>,
)

@Serializable
data class WebPlayerStatistics(
    val average: Double,
    val total: Int,
    val contractAverages: Map<@Serializable(with = UUIDSerializer::class) UUID, Double>,
    val scoreDistribution: Map<String, Int>,
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

fun AuthToken.toTokenResponse() = TokenResponse(
    token = token,
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

fun Player.toWebPlayer() = when (this) {
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

private fun PlayerAverage.toWebPlayerAverage() = WebPlayerAverage(
    playerId = participation.playerId,
    displayName = participation.displayName,
    average = average.average,
    weightedAverage = weightedAverage.average,
)

fun TeamScores.toWebTeamScores() = WebTeamScores(team1?.score, team2?.score)

fun GameStatistics.toWebGameStatistics() = WebGameStatistics(
    playerAverages = playerAverages.map { it.toWebPlayerAverage() },
    team1Average = teamAverages.team1.average,
    team2Average = teamAverages.team2.average,
    team1WeightedAverage = weightedTeamAverages.team1.average,
    team2WeightedAverage = weightedTeamAverages.team2.average,
    expectedScoresOverTime = expectedScoresOverTime.map { it.toWebTeamScores() },
)

fun PlayerStatistics.toWebPlayerStatistics() = WebPlayerStatistics(
    average = average.average,
    total = total.score,
    contractAverages = contractAverages.mapValues { it.value.average },
    scoreDistribution = scoreDistribution.asIterable().associate { it.key.score.toString() to it.value },
)

fun TableStatistics.toWebTableStatistics() = WebTableStatistics(
    playerAverages = playerAverages.map { it.toWebPlayerAverage() },
    contractAverages = contractAverages.mapValues { it.value.average },
    scoresOverTime = scoresOverTime.map { WebGameScoreSummary(it.gameId, it.total.toWebTeamScores()) }
)
