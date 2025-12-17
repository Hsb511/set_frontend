package com.team23.ui.game

sealed class GameCompletionType(
    val action: GameAction,
    val label: String,
) {

    data object Restart: GameCompletionType(
        action = GameAction.Restart,
        label = "Play solo again",
    )

    data object Retry: GameCompletionType(
        action = GameAction.RetryConfirmation,
        label = "Try confirming again",
    )
}
