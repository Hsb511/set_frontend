package com.team23.ui.game

import com.team23.domain.game.model.MultiGameMessage
import com.team23.ui.gameSelection.MultiGameMode
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds
import kotlin.time.Instant

class EndGameUiMapperTest {

    private lateinit var mapper: EndGameUiMapper

    @BeforeTest
    fun setup() {
        mapper = EndGameUiMapper()
    }

    @Test
    fun `Given multiGameMode is null and isConfirmed is true, When mapping to simple end game, Then returns Restart`() {
        // When
        val result = mapper.toSimpleEndGame(multiGameMode = null, isConfirmed = true)

        // Then
        assertEquals(EndGameUiModel.Restart, result)
    }

    @Test
    fun `Given multiGameMode is null and isConfirmed is false, When mapping to simple end game, Then returns Retry`() {
        // When
        val result = mapper.toSimpleEndGame(multiGameMode = null, isConfirmed = false)

        // Then
        assertEquals(EndGameUiModel.Retry, result)
    }

    @Test
    fun `Given multiGameMode is TimeTrial, When mapping to simple end game, Then returns TimeTrial`() {
        // When
        val resultConfirmed = mapper.toSimpleEndGame(multiGameMode = MultiGameMode.TimeTrial, isConfirmed = true)
        val resultNotConfirmed = mapper.toSimpleEndGame(multiGameMode = MultiGameMode.TimeTrial, isConfirmed = false)

        // Then
        assertEquals(EndGameUiModel.TimeTrial, resultConfirmed)
        assertEquals(EndGameUiModel.TimeTrial, resultNotConfirmed)
    }

    @Test
    fun `Given multiGameMode is Versus and isConfirmed is true, When mapping to simple end game, Then returns Restart`() {
        // When
        val result = mapper.toSimpleEndGame(multiGameMode = MultiGameMode.Versus, isConfirmed = true)

        // Then
        assertEquals(EndGameUiModel.Restart, result)
    }

    @Test
    fun `Given multiGameMode is Versus and isConfirmed is false, When mapping to simple end game, Then returns Retry`() {
        // When
        val result = mapper.toSimpleEndGame(multiGameMode = MultiGameMode.Versus, isConfirmed = false)

        // Then
        assertEquals(EndGameUiModel.Retry, result)
    }

    @Test
    fun `Given GameCompleted with turns, When mapping to scores, Then data is ordered from highest turns to lowest`() {
        // Given
        val message = createGameCompleted(
            winnerUsername = "alice",
            scores = MultiGameMessage.GameCompleted.Scores.WithTurns(
                turnsByPlayer = mapOf("bob" to 3, "alice" to 10, "charlie" to 7)
            ),
        )

        // When
        val result = mapper.toEndGameScores(message)

        // Then
        assertEquals("alice", result.winnerUsername)
        assertEquals(listOf("Player", "turns"), result.header)
        assertEquals(3, result.data.size)
        assertEquals(listOf("alice", "10"), result.data[0])
        assertEquals(listOf("charlie", "7"), result.data[1])
        assertEquals(listOf("bob", "3"), result.data[2])
    }

    @Test
    fun `Given GameCompleted with single player turns, When mapping to scores, Then data contains one entry`() {
        // Given
        val message = createGameCompleted(
            winnerUsername = "alice",
            scores = MultiGameMessage.GameCompleted.Scores.WithTurns(
                turnsByPlayer = mapOf("alice" to 5)
            ),
        )

        // When
        val result = mapper.toEndGameScores(message)

        // Then
        assertEquals(1, result.data.size)
        assertEquals(listOf("alice", "5"), result.data[0])
    }

    @Test
    fun `Given GameCompleted with equal turns, When mapping to scores, Then data contains all players`() {
        // Given
        val message = createGameCompleted(
            winnerUsername = "alice",
            scores = MultiGameMessage.GameCompleted.Scores.WithTurns(
                turnsByPlayer = mapOf("alice" to 5, "bob" to 5)
            ),
        )

        // When
        val result = mapper.toEndGameScores(message)

        // Then
        assertEquals(2, result.data.size)
        // Both have same turns, order between them is stable but both present
        assertEquals("5", result.data[0][1])
        assertEquals("5", result.data[1][1])
    }

    @Test
    fun `Given GameCompleted with times, When mapping to scores, Then data is ordered from smallest duration to largest`() {
        // Given
        val message = createGameCompleted(
            winnerUsername = "alice",
            scores = MultiGameMessage.GameCompleted.Scores.WithTimes(
                timesByPlayer = mapOf(
                    "bob" to 5.minutes,
                    "alice" to 1.minutes + 30.seconds,
                    "charlie" to 3.minutes,
                )
            ),
        )

        // When
        val result = mapper.toEndGameScores(message)

        // Then
        assertEquals("alice", result.winnerUsername)
        assertEquals(listOf("Player", "times"), result.header)
        assertEquals(3, result.data.size)
        assertEquals("alice", result.data[0][0])
        assertEquals("charlie", result.data[1][0])
        assertEquals("bob", result.data[2][0])
    }

    @Test
    fun `Given GameCompleted with single player time, When mapping to scores, Then data contains one entry`() {
        // Given
        val message = createGameCompleted(
            winnerUsername = "alice",
            scores = MultiGameMessage.GameCompleted.Scores.WithTimes(
                timesByPlayer = mapOf("alice" to 2.minutes)
            ),
        )

        // When
        val result = mapper.toEndGameScores(message)

        // Then
        assertEquals(1, result.data.size)
        assertEquals("alice", result.data[0][0])
    }

    @Test
    fun `Given GameCompleted with turns, When mapping to scores, Then content is Table with correct header and data`() {
        // Given
        val message = createGameCompleted(
            winnerUsername = "alice",
            scores = MultiGameMessage.GameCompleted.Scores.WithTurns(
                turnsByPlayer = mapOf("alice" to 10, "bob" to 3)
            ),
        )

        // When
        val result = mapper.toEndGameScores(message)

        // Then
        val content = result.content
        assertEquals(
            EndGameUiModel.Content.Table(
                header = listOf("Player", "turns"),
                data = listOf(listOf("alice", "10"), listOf("bob", "3")),
            ),
            content,
        )
    }

    @Test
    fun `Given GameCompleted, When mapping to scores, Then label contains winner username`() {
        // Given
        val message = createGameCompleted(
            winnerUsername = "charlie",
            scores = MultiGameMessage.GameCompleted.Scores.WithTurns(
                turnsByPlayer = mapOf("charlie" to 8)
            ),
        )

        // When
        val result = mapper.toEndGameScores(message)

        // Then
        assertEquals("winnerUsername won the game!", result.label)
    }

    private fun createGameCompleted(
        winnerUsername: String,
        scores: MultiGameMessage.GameCompleted.Scores,
        timestamp: Instant = Instant.fromEpochMilliseconds(0),
    ) = MultiGameMessage.GameCompleted(
        timestamp = timestamp,
        winnerUsername = winnerUsername,
        scores = scores,
    )
}
