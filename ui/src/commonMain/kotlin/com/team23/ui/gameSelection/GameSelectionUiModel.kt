package com.team23.ui.gameSelection

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.People
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.ui.graphics.vector.ImageVector
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
sealed interface GameSelectionUiModel {
    data object Loading : GameSelectionUiModel

    data class Data(
        val hasAnOngoingSoloGame: Boolean,
        val multiGames: List<MultiGame> = emptyList(),
    ) : GameSelectionUiModel {

        val hasMultiGames: Boolean
            get() = multiGames.isNotEmpty()

        data class MultiGame(
            val publicName: String,
            val playersCount: Int,
            val type: Type,
        ) {
            enum class Type(val imageVector: ImageVector) {
                TimeTrial(Icons.Outlined.Timer),
                Versus(Icons.Outlined.People),
            }
        }
    }
}
