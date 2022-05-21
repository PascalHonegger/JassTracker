package dev.honegger.jasstracker.data.repositories

import dev.honegger.jasstracker.domain.*
import dev.honegger.jasstracker.domain.util.toUUID
import kotlinx.datetime.LocalDateTime
import java.util.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull

class GameRepositoryImplTest : RepositoryTest() {
    private val repo = GameRepositoryImpl()
    private val game1 = Game(
        "3de81ae0-792e-43b0-838b-acad78f29ba6".toUUID(),
        startTime = LocalDateTime.parse("2022-03-31T14:08:59.654385"),
        endTime = LocalDateTime.parse("2022-04-01T14:08:59.654385"),
        team1 = Team(
            GameParticipation("27fa77f3-eb56-46a0-8ada-b0a6f2e26cc0".toUUID(), "Pascal"),
            GameParticipation("283c0a20-b293-40e7-8858-da098a53b756".toUUID(), "David"),
        ),
        team2 = Team(
            GameParticipation("3095c042-d0a9-4219-9f65-53d4565fd1e6".toUUID(), "Marcel"),
            GameParticipation("cdfa5ae5-d182-4e11-b7f1-a173b2b4b797".toUUID(), "Jamie"),
        ),
        rounds = listOf(
            Round("c1dbf7ae-719f-4acc-a6e6-03c37534e8a4".toUUID(),
                1,
                120,
                "3de81ae0-792e-43b0-838b-acad78f29ba6".toUUID(),
                "27fa77f3-eb56-46a0-8ada-b0a6f2e26cc0".toUUID(),
                "58bae0f8-8c59-4a40-aa2d-9c6a489366b3".toUUID()),
            Round("22e854d0-b00b-4dc5-bf72-1ec97ebd0fdf".toUUID(),
                2,
                73,
                "3de81ae0-792e-43b0-838b-acad78f29ba6".toUUID(),
                "3095c042-d0a9-4219-9f65-53d4565fd1e6".toUUID(),
                "d895b400-3d89-48db-a7ed-5e593f54b7f6".toUUID()),
            Round("dc4a1ebc-68d1-4130-86f7-142d293af28a".toUUID(),
                3,
                89,
                "3de81ae0-792e-43b0-838b-acad78f29ba6".toUUID(),
                "283c0a20-b293-40e7-8858-da098a53b756".toUUID(),
                "41c7bd00-3da4-4926-bcb6-08e12aafbe6d".toUUID()),
            Round("623a1692-fe3a-42da-977e-dea4bb872112".toUUID(),
                4,
                117,
                "3de81ae0-792e-43b0-838b-acad78f29ba6".toUUID(),
                "cdfa5ae5-d182-4e11-b7f1-a173b2b4b797".toUUID(),
                "38fb8cbb-b22d-40f7-b9a1-b4adc1740075".toUUID()),
            Round("94615cd2-1cd7-4298-b7e1-7db6d5e5fb8e".toUUID(),
                5,
                157,
                "3de81ae0-792e-43b0-838b-acad78f29ba6".toUUID(),
                "27fa77f3-eb56-46a0-8ada-b0a6f2e26cc0".toUUID(),
                "62aeb3b0-7b2d-4670-9789-6acd23fb8609".toUUID()),
            Round("fddcb3aa-6f8f-4656-be2e-ab9d82b5b675".toUUID(),
                6,
                146,
                "3de81ae0-792e-43b0-838b-acad78f29ba6".toUUID(),
                "3095c042-d0a9-4219-9f65-53d4565fd1e6".toUUID(),
                "5a8de6ea-8da6-4c2b-b572-3d2335a7cbe2".toUUID()),
            Round("69884b8a-df0c-48fa-9ad8-065cc16740f5".toUUID(),
                7,
                104,
                "3de81ae0-792e-43b0-838b-acad78f29ba6".toUUID(),
                "283c0a20-b293-40e7-8858-da098a53b756".toUUID(),
                "168b6602-07c3-4600-b39a-d08aca3323b0".toUUID()),
            Round("e5847e97-61bd-4b41-813b-80995e4ea2b9".toUUID(),
                8,
                73,
                "3de81ae0-792e-43b0-838b-acad78f29ba6".toUUID(),
                "cdfa5ae5-d182-4e11-b7f1-a173b2b4b797".toUUID(),
                "02c40574-edd7-4a5b-baeb-e15cd50b1387".toUUID()),
            Round("0efda358-f340-4c43-85b1-f69def6b34d1".toUUID(),
                9,
                157,
                "3de81ae0-792e-43b0-838b-acad78f29ba6".toUUID(),
                "27fa77f3-eb56-46a0-8ada-b0a6f2e26cc0".toUUID(),
                "28d6e9ac-fc8e-4dad-8af8-2ae126b8d691".toUUID()),
            Round("6af5adf2-aaa5-4583-8101-c16122bb347f".toUUID(),
                10,
                146,
                "3de81ae0-792e-43b0-838b-acad78f29ba6".toUUID(),
                "3095c042-d0a9-4219-9f65-53d4565fd1e6".toUUID(),
                "345bde8f-a316-4952-b021-7cbe7ad62306".toUUID()),
            Round("1705f2cb-0ff8-4361-9bcb-4ff1a2e3f7dd".toUUID(),
                11,
                89,
                "3de81ae0-792e-43b0-838b-acad78f29ba6".toUUID(),
                "283c0a20-b293-40e7-8858-da098a53b756".toUUID(),
                "345bde8f-a316-4952-b021-7cbe7ad62306".toUUID()),
            Round("d695e65a-a546-4ef8-ac11-8806f3e35a0c".toUUID(),
                12,
                73,
                "3de81ae0-792e-43b0-838b-acad78f29ba6".toUUID(),
                "cdfa5ae5-d182-4e11-b7f1-a173b2b4b797".toUUID(),
                "28d6e9ac-fc8e-4dad-8af8-2ae126b8d691".toUUID()),
            Round("fdd91c1f-af05-40a1-8b39-ecf5d3291556".toUUID(),
                13,
                157,
                "3de81ae0-792e-43b0-838b-acad78f29ba6".toUUID(),
                "27fa77f3-eb56-46a0-8ada-b0a6f2e26cc0".toUUID(),
                "5a8de6ea-8da6-4c2b-b572-3d2335a7cbe2".toUUID()),
            Round("4c364d4a-7b86-4f72-9336-f86754b0a928".toUUID(),
                14,
                146,
                "3de81ae0-792e-43b0-838b-acad78f29ba6".toUUID(),
                "3095c042-d0a9-4219-9f65-53d4565fd1e6".toUUID(),
                "62aeb3b0-7b2d-4670-9789-6acd23fb8609".toUUID()),
            Round("ce2c29ce-cd3f-4140-b439-0cde812f2598".toUUID(),
                15,
                120,
                "3de81ae0-792e-43b0-838b-acad78f29ba6".toUUID(),
                "283c0a20-b293-40e7-8858-da098a53b756".toUUID(),
                "02c40574-edd7-4a5b-baeb-e15cd50b1387".toUUID()),
            Round("8cff9836-17f1-4b30-9457-f7ae59088f70".toUUID(),
                16,
                157,
                "3de81ae0-792e-43b0-838b-acad78f29ba6".toUUID(),
                "cdfa5ae5-d182-4e11-b7f1-a173b2b4b797".toUUID(),
                "168b6602-07c3-4600-b39a-d08aca3323b0".toUUID()),
            Round("96ad63e5-c942-4b7c-bcc3-0f7b89b47468".toUUID(),
                17,
                157,
                "3de81ae0-792e-43b0-838b-acad78f29ba6".toUUID(),
                "27fa77f3-eb56-46a0-8ada-b0a6f2e26cc0".toUUID(),
                "d895b400-3d89-48db-a7ed-5e593f54b7f6".toUUID()),
            Round("8d708031-46af-4475-8697-be8692043357".toUUID(),
                18,
                104,
                "3de81ae0-792e-43b0-838b-acad78f29ba6".toUUID(),
                "3095c042-d0a9-4219-9f65-53d4565fd1e6".toUUID(),
                "58bae0f8-8c59-4a40-aa2d-9c6a489366b3".toUUID()),
            Round("98de7a69-f7ff-45fa-9aa5-10e02728ee2c".toUUID(),
                19,
                73,
                "3de81ae0-792e-43b0-838b-acad78f29ba6".toUUID(),
                "283c0a20-b293-40e7-8858-da098a53b756".toUUID(),
                "38fb8cbb-b22d-40f7-b9a1-b4adc1740075".toUUID()),
            Round("2e4887c9-3fbe-441a-9992-0da5f5a2fc8e".toUUID(),
                20,
                89,
                "3de81ae0-792e-43b0-838b-acad78f29ba6".toUUID(),
                "cdfa5ae5-d182-4e11-b7f1-a173b2b4b797".toUUID(),
                "41c7bd00-3da4-4926-bcb6-08e12aafbe6d".toUUID()),
        )
    )
    private val game2 = Game(
        "85df0ff4-6c8b-4846-b8e6-400940660f0b".toUUID(),
        startTime = LocalDateTime.parse("2022-03-31T14:08:59.654385"),
        endTime = null,
        team1 = Team(
            GameParticipation("665032ec-8c6a-4ff7-a5e1-ea5a705ef0b3".toUUID(), "Guest 1"),
            GameParticipation("27fa77f3-eb56-46a0-8ada-b0a6f2e26cc0".toUUID(), "Pascal")
        ),
        team2 = Team(
            GameParticipation("7dad81d3-62db-4553-9d48-f38f404f1596".toUUID(), "Guest 2"),
            GameParticipation("3095c042-d0a9-4219-9f65-53d4565fd1e6".toUUID(), "Marcel")
        ),
        rounds = listOf(
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
    )

    @Test
    fun `getGameOrNull returns game`() {
        assertEquals(game2, repo.getGameOrNull(game2.id))
    }

    @Test
    fun `getAllGames returns all games`() {
        assertEquals(setOf(game1, game2), repo.getAllGames().toSet())
    }

    @Test
    fun `getAllGamesOfTable returns games`() {
        val tableId = "de940c47-9881-4e95-bc3d-6014ad1902e1".toUUID()
        assertEquals(listOf(game2), repo.getAllGamesOfTable(tableId))
    }

    @Test
    fun `saveGame saves game`() {
        val id = UUID.randomUUID()
        val game = Game(
            id = id,
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
        repo.saveGame(game, "92968e55-6df0-4f21-a7cc-a243025e5f87".toUUID())
        assertEquals(game, repo.getGameOrNull(id))
    }

    @Test
    fun `updateGame updates game`() {
        val id = UUID.randomUUID()
        val game = Game(
            id = id,
            startTime = LocalDateTime(2022, 4,1, 13, 37),
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
        repo.saveGame(game, "de940c47-9881-4e95-bc3d-6014ad1902e1".toUUID())
        val updatedGame = game.copy(endTime = LocalDateTime(2022, 4,20, 13, 37))
        repo.updateGame(updatedGame)
        assertEquals(updatedGame, repo.getGameOrNull(id))
    }

    @Test
    fun `deleteGame deletes game`() {
        val id = UUID.randomUUID()
        val game = Game(
            id = id,
            startTime = LocalDateTime(2022, 4,1, 13, 37),
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
        repo.saveGame(game, "de940c47-9881-4e95-bc3d-6014ad1902e1".toUUID())
        repo.deleteGameById(id)
        assertNull(repo.getGameOrNull(id))
    }

    @Test
    fun `deleteGame returns false if no round is found for id`() {
        val result = repo.deleteGameById(UUID.randomUUID())
        assertFalse { result }
    }

    @Test
    fun `getGroupedGamesOfTables returns correctly grouped games`() {
        val tableId1 = "92968e55-6df0-4f21-a7cc-a243025e5f87".toUUID()
        val tableId2 = "de940c47-9881-4e95-bc3d-6014ad1902e1".toUUID()
        val expected = mapOf(tableId1 to listOf(game1), tableId2 to listOf(game2))
        assertEquals(expected, repo.getGroupedGamesOfTables(listOf(tableId1, tableId2)))
    }
}
