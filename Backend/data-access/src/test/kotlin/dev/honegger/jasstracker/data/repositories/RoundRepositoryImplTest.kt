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
        val id = "c1dbf7ae-719f-4acc-a6e6-03c37534e8a4".toUUID()
        val expected = Round(
            id,
            1,
            120,
            "3de81ae0-792e-43b0-838b-acad78f29ba6".toUUID(),
            "27fa77f3-eb56-46a0-8ada-b0a6f2e26cc0".toUUID(),
            "58bae0f8-8c59-4a40-aa2d-9c6a489366b3".toUUID()
        )
        assertEquals(expected, repo.getRoundOrNull(id))
    }

    @Test
    fun `getRoundsForGame returns rounds`() {
        val gameId = "85df0ff4-6c8b-4846-b8e6-400940660f0b".toUUID()
        val expected = listOf(
            Round("e1eb6701-f163-4e54-aadd-8239476972f6".toUUID(),
                1,
                157,
                "85df0ff4-6c8b-4846-b8e6-400940660f0b".toUUID(),
                "3095c042-d0a9-4219-9f65-53d4565fd1e6".toUUID(),
                "58bae0f8-8c59-4a40-aa2d-9c6a489366b3".toUUID()),
            Round("e515e48e-ffe9-4ac4-b5bc-c9a74568ffa5".toUUID(),
                2,
                89,
                "85df0ff4-6c8b-4846-b8e6-400940660f0b".toUUID(),
                "665032ec-8c6a-4ff7-a5e1-ea5a705ef0b3".toUUID(),
                "38fb8cbb-b22d-40f7-b9a1-b4adc1740075".toUUID()),
            Round("e74dfd78-d14f-468f-b9a0-423264725d3b".toUUID(),
                3,
                73,
                "85df0ff4-6c8b-4846-b8e6-400940660f0b".toUUID(),
                "7dad81d3-62db-4553-9d48-f38f404f1596".toUUID(),
                "d895b400-3d89-48db-a7ed-5e593f54b7f6".toUUID())
        )
        assertEquals(expected, repo.getRoundsForGame(gameId))
    }

    @Test
    fun `saveRound saves round`() {
        val game = createEmptyGame()
        val id = UUID.randomUUID()
        val round = Round(
            id,
            1,
            120,
            game.id,
            game.team1.player1.playerId,
            "58bae0f8-8c59-4a40-aa2d-9c6a489366b3".toUUID()
        )
        repo.saveRound(round)
        assertEquals(round, repo.getRoundOrNull(id))
        assertEquals(listOf(round), repo.getRoundsForGame(game.id))
    }

    @Test
    fun `updateRound updates round`() {
        val game = createEmptyGame()
        val id = UUID.randomUUID()
        val round = Round(
            id,
            1,
            120,
            game.id,
            game.team1.player1.playerId,
            "58bae0f8-8c59-4a40-aa2d-9c6a489366b3".toUUID()
        )
        repo.saveRound(round)
        val updatedRound = round.copy(score = 121)
        repo.updateRound(updatedRound)
        assertEquals(updatedRound, repo.getRoundOrNull(id))
        assertEquals(listOf(updatedRound), repo.getRoundsForGame(game.id))
    }

    @Test
    fun `deleteRound updates higher numbers`() {
        val game = createEmptyGame()
        val id = UUID.randomUUID()
        val round = Round(
            id,
            1,
            120,
            game.id,
            game.team1.player1.playerId,
            "58bae0f8-8c59-4a40-aa2d-9c6a489366b3".toUUID()
        )
        val round2 = Round(
            UUID.randomUUID(),
            1,
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
    }

    private fun createEmptyGame(): Game {
        val game = Game(
            id = UUID.randomUUID(),
            startTime = LocalDateTime(2022, 4,20, 13, 37),
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

    @Test
    fun `deleteRound removes round`() {
        val game = createEmptyGame()
        val id = UUID.randomUUID()
        val round = Round(
            id,
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
}
