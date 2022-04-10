package dev.honegger.endpoints

import dev.honegger.domain.*
import dev.honegger.serializer.UUIDSerializer
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.Serializable
import java.util.*

// Would be loaded once authentication is implemented
val dummySession = UserSession(userId = UUID.fromString("27fa77f3-eb56-46a0-8ada-b0a6f2e26cc0"), "dummy")

// These objects could be generated using something like OpenAPI
@Serializable
data class WebTable(
    @Serializable(with = UUIDSerializer::class)
    val id: UUID,
    val name: String,
    @Serializable(with = UUIDSerializer::class)
    val ownerId: UUID,
    val gameIds: List<@Serializable(with = UUIDSerializer::class) UUID>,
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

@Serializable
data class WebContract(
    @Serializable(with = UUIDSerializer::class)
    val id: UUID,
    val name: String,
    val multiplier: Int,
    val type: ContractType,
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
    gameIds = games.map { it.id }
)

fun WebGame.toGame() = Game(
    id = id,
    startTime = startTime.toLocalDateTime(TimeZone.UTC),
    endTime = endTime?.toLocalDateTime(TimeZone.UTC),
)
fun Game.toWebGame() = WebGame(
    id = id,
    startTime = startTime.toInstant(TimeZone.UTC),
    endTime = endTime?.toInstant(TimeZone.UTC),
)

fun Contract.toWebContract() = WebContract(
    id = id,
    name = name,
    multiplier = multiplier,
    type = type,
)
