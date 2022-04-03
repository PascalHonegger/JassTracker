package dev.honegger.endpoints

import dev.honegger.domain.Game
import dev.honegger.domain.Table
import dev.honegger.domain.UserSession
import dev.honegger.serializer.UUIDSerializer
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.Serializable
import java.util.*

// Would be loaded once authentication is implemented
val dummySession = UserSession(userId = UUID.fromString("2ea1cb74-a9aa-4b81-8953-d7a16d6ba582"), "dummy")

// These objects could be generated using something like OpenAPI
@Serializable
data class WebTable(
    @Serializable(with = UUIDSerializer::class)
    val id: UUID,
    val name: String,
    @Serializable(with = UUIDSerializer::class)
    val ownerId: UUID,
    val games: List<WebGame>,
)

@Serializable
data class WebCreateTable(val name: String)

@Serializable
data class WebGame(
    @Serializable(with = UUIDSerializer::class)
    val id: UUID,
    val startTime: Instant,
    val endTime: Instant?,
)

@Serializable
data class WebCreateGame(val tableId: String)

// Map from WebTable to domain Table
fun WebTable.toTable() = Table(
    id = id,
    name = name,
    ownerId = ownerId,
    games = games.map { it.toGame() },
)

// Map from domain Table to WebTable
fun Table.toWebTable() = WebTable(
    id = id,
    name = name,
    ownerId = ownerId,
    games = games.map { it.toWebGame() }
)

// Map from WebGame to domain Game
fun WebGame.toGame() = Game(
    id = id,
    startTime = startTime.toLocalDateTime(TimeZone.UTC),
    endTime = endTime?.toLocalDateTime(TimeZone.UTC),
)

// Map from domain Game to WebGame
fun Game.toWebGame() = WebGame(
    id = id,
    startTime = startTime.toInstant(TimeZone.UTC),
    endTime = endTime?.toInstant(TimeZone.UTC),
)
