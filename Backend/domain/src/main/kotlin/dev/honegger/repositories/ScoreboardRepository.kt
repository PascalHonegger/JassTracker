package dev.honegger.repositories

import dev.honegger.domain.Scoreboard

interface ScoreboardRepository {
    fun getScoreboardOrNull(id: String): Scoreboard?
    fun saveScoreboard(updatedScoreboard: Scoreboard)
}
