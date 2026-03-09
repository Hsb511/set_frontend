package com.team23.ui.game

sealed class EndGameUiModel(
    val content: Content,
    val label: String,
) {

    sealed interface Content {
        data class Action(val action: GameAction) : Content
        data object Loader : Content
        data class Table(
            val header: List<String>,
            val data: List<List<String>>,
        ) : Content
    }

    data object Restart : EndGameUiModel(
        content = Content.Action(GameAction.Restart),
        label = "Play solo again",
    )

    data object Retry : EndGameUiModel(
        content = Content.Action(GameAction.RetryConfirmation),
        label = "Try confirming again",
    )

    data object TimeTrial : EndGameUiModel(
        content = Content.Loader,
        label = "Waiting for opponents to finish"
    )

    data class Scores(
        val winnerUsername: String,
        val header: List<String>,
        val data: List<List<String>>,
    ) : EndGameUiModel(
        content = Content.Table(
            header = header,
            data = data,
        ),
        label = "winnerUsername won the game!",
    )
}
