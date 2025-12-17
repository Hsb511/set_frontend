package com.team23.ui.lobby

import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
data class LobbyUiModel(
    val hasAnOngoingSoloGame: Boolean,
    val multiGames: List<MultiGame>,
) {

    val hasMultiGames: Boolean
        get() = multiGames.isNotEmpty()

    data class MultiGame(
        val gameId: Uuid,
        val hostName: String,
        val playersCount: Int,
    )
}
