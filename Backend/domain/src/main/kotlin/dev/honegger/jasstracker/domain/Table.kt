package dev.honegger.jasstracker.domain

import java.util.UUID
import kotlin.Comparator

data class Table(
    val id: UUID,
    val name: String,
    val ownerId: UUID,
    val games: List<Game>,
)

val Table.latestGame: Game? get() {
    val latestComparator = Comparator.comparing { g: Game -> g.endTime == null }.thenComparing { g: Game -> g.startTime }
    return games.maxWithOrNull(latestComparator)
}
