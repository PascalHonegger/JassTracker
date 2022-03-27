package dev.honegger.repositories

import dev.honegger.domain.Scoreboard

class ScoreboardRepositoryImpl : ScoreboardRepository {
    private val scoreBoards = mutableMapOf<String, Scoreboard>()

    override fun getScoreboardOrNull(id: String): Scoreboard? {
        // Placeholder code for DB access
        // This code would map the generated jOOQ-Entities to the domain object
        return scoreBoards[id]
    }

    override fun saveScoreboard(updatedScoreboard: Scoreboard) {
        // Placeholder code for DB access
        // This code would map the provided domain object to the generated jOOQ-Entities
        scoreBoards[updatedScoreboard.id] = updatedScoreboard
    }
}
