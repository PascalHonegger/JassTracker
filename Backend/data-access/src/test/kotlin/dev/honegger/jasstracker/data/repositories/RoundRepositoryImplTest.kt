package dev.honegger.jasstracker.data.repositories

import dev.honegger.jasstracker.domain.Game
import dev.honegger.jasstracker.domain.GameParticipation
import dev.honegger.jasstracker.domain.Round
import dev.honegger.jasstracker.domain.Team
import dev.honegger.jasstracker.domain.util.toUUID
import kotlinx.datetime.LocalDateTime
import java.util.UUID
import kotlin.test.*

class RoundRepositoryImplTest : RepositoryTest() {
    private val repo = RoundRepositoryImpl()

    @Test
    fun `getRound returns round`() {
        val repo = RoundRepositoryImpl()
        val round = Round(
            UUID.randomUUID(),
            1,
            120,
            "3de81ae0-792e-43b0-838b-acad78f29ba6".toUUID(),
            "27fa77f3-eb56-46a0-8ada-b0a6f2e26cc0".toUUID(),
            "58bae0f8-8c59-4a40-aa2d-9c6a489366b3".toUUID()
        )
        repo.saveRound(round)
        assertEquals(round, repo.getRoundOrNull(round.id))
        repo.deleteRoundById(round.id)
        assertNull(repo.getRoundOrNull(round.id))
    }

    @Test
    fun `saveRound saves round`() {
        val game = createEmptyGame()
        val round = Round(
            UUID.randomUUID(),
            1,
            120,
            game.id,
            game.team1.player1.playerId,
            "58bae0f8-8c59-4a40-aa2d-9c6a489366b3".toUUID()
        )
        repo.saveRound(round)
        assertEquals(round, repo.getRoundOrNull(round.id))
        repo.deleteRoundById(round.id)
    }

    @Test
    fun `updateRound updates round`() {
        val game = createEmptyGame()
        val round = Round(
            UUID.randomUUID(),
            1,
            120,
            game.id,
            game.team1.player1.playerId,
            "58bae0f8-8c59-4a40-aa2d-9c6a489366b3".toUUID()
        )
        repo.saveRound(round)
        val updatedRound = round.copy(score = 121)
        repo.updateRound(updatedRound)
        assertEquals(updatedRound, repo.getRoundOrNull(round.id))
        repo.deleteRoundById(round.id)
    }

    @Test
    fun `deleteRound updates higher numbers`() {
        val game = createEmptyGame()
        val round = Round(
            UUID.randomUUID(),
            1,
            120,
            game.id,
            game.team1.player1.playerId,
            "58bae0f8-8c59-4a40-aa2d-9c6a489366b3".toUUID()
        )
        val round2 = Round(
            UUID.randomUUID(),
            2,
            120,
            game.id,
            game.team1.player2.playerId,
            "58bae0f8-8c59-4a40-aa2d-9c6a489366b3".toUUID()
        )
        repo.saveRound(round)
        repo.saveRound(round2)
        repo.deleteRoundById(round.id)
        val loadedRound = repo.getRoundOrNull(round2.id)
        assertNotNull(loadedRound)
        assertEquals(1, loadedRound.number)
        repo.deleteRoundById(round2.id)
        assertNull(repo.getRoundOrNull(round.id))
        assertNull(repo.getRoundOrNull(round2.id))
    }

    @Test
    fun `deleteRound removes round`() {
        val game = createEmptyGame()
        val round = Round(
            UUID.randomUUID(),
            1,
            120,
            game.id,
            game.team1.player1.playerId,
            "58bae0f8-8c59-4a40-aa2d-9c6a489366b3".toUUID()
        )
        repo.saveRound(round)
        repo.deleteRoundById(round.id)
        assertNull(repo.getRoundOrNull(round.id))
    }

    private fun createEmptyGame(): Game {
        val game = Game(
            id = UUID.randomUUID(),
            startTime = LocalDateTime(2022, 4, 20, 13, 37),
            endTime = null,
            rounds = emptyList(),
            team1 = Team(
                GameParticipation("27fa77f3-eb56-46a0-8ada-b0a6f2e26cc0".toUUID(), "Pascal"),
                GameParticipation("3095c042-d0a9-4219-9f65-53d4565fd1e6".toUUID(), "Marcel")
            ),
            team2 = Team(
                GameParticipation("665032ec-8c6a-4ff7-a5e1-ea5a705ef0b3".toUUID(), "Guest 1"),
                GameParticipation("7dad81d3-62db-4553-9d48-f38f404f1596".toUUID(), "Guest 2")
            )
        )
        GameRepositoryImpl().saveGame(game, "92968e55-6df0-4f21-a7cc-a243025e5f87".toUUID())
        return game
    }
}
