package dev.honegger.services

import dev.honegger.domain.Scoreboard
import dev.honegger.domain.UserSession
import dev.honegger.repositories.ScoreboardRepository
import mu.KotlinLogging
import java.util.*

interface ScoreboardService {
    fun createScoreboard(session: UserSession, name: String): Scoreboard
    fun getScoreboardOrNull(session: UserSession, id: String): Scoreboard?
    fun updateScoreboard(session: UserSession, updatedScoreboard: Scoreboard)
}

private val log = KotlinLogging.logger { }

class ScoreboardServiceImpl(private val scoreboardRepository: ScoreboardRepository) :
    ScoreboardService {
    override fun createScoreboard(
        session: UserSession,
        name: String,
    ): Scoreboard {
        // Example of user input validation
        check(name.length in 2..15) { "Name must be between 2 and 15 characters" }
        val newScoreboard = Scoreboard(
            id = UUID.randomUUID().toString(),
            name = name,
            ownerId = session.userId,
        )

        // Example of a log message
        log.info { "Saving new scoreboard $newScoreboard" }
        scoreboardRepository.saveScoreboard(newScoreboard)
        return newScoreboard
    }

    override fun getScoreboardOrNull(
        session: UserSession,
        id: String,
    ): Scoreboard? {
        // Users can load any scoreboard they know the ID of
        return scoreboardRepository.getScoreboardOrNull(id)
    }

    override fun updateScoreboard(
        session: UserSession,
        updatedScoreboard: Scoreboard,
    ) {
        val existingScoreboard =
            scoreboardRepository.getScoreboardOrNull(updatedScoreboard.id)
        // User can only update a scoreboard which exists and is owned by himself
        checkNotNull(existingScoreboard)
        check(updatedScoreboard.ownerId == session.userId)
        // Only copy name as an example if only partial update is allowed
        scoreboardRepository.saveScoreboard(existingScoreboard.copy(name = updatedScoreboard.name))
    }
}
