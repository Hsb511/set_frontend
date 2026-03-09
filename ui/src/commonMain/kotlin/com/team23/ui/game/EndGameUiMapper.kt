package com.team23.ui.game

import com.team23.domain.game.model.MultiGameMessage
import com.team23.ui.gameSelection.MultiGameMode

class EndGameUiMapper {

    fun toSimpleEndGame(multiGameMode: MultiGameMode?, isConfirmed: Boolean): EndGameUiModel = when {
        multiGameMode == MultiGameMode.TimeTrial -> EndGameUiModel.TimeTrial
        isConfirmed -> EndGameUiModel.Restart
        else -> EndGameUiModel.Retry
    }

    fun toEndGameScores(message: MultiGameMessage.GameCompleted): EndGameUiModel.Scores {
        val data = when (val scores = message.scores) {
            is MultiGameMessage.GameCompleted.Scores.WithTurns -> scores.turnsByPlayer
                .entries
                .sortedByDescending { it.value }
                .map { (player, turns) -> listOf(player, turns.toString()) }

            is MultiGameMessage.GameCompleted.Scores.WithTimes -> scores.timesByPlayer
                .entries
                .sortedBy { it.value }
                .map { (player, duration) -> listOf(player, duration.toString()) }
        }

        return EndGameUiModel.Scores(
            winnerUsername = message.winnerUsername,
            header = listOf("Player", message.scores.label),
            data = data,
        )
    }
}

