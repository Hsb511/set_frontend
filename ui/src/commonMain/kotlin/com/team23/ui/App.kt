package com.team23.ui

import androidx.compose.runtime.Composable
import com.team23.ui.card.CardUiModel
import com.team23.ui.gameZone.GameZone
import com.team23.ui.gameZone.GameZoneUiModel
import com.team23.ui.shape.FillingTypeUiModel
import com.team23.ui.theming.SetTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    SetTheme {
        GameZone(
            gameZone = GameZoneUiModel(
                cards = List(12) { index ->
                    CardUiModel(
                        patternAmount = (1..3).random(),
                        color = CardUiModel.Color.entries.random(),
                        fillingType = FillingTypeUiModel.entries.random(),
                        shape = CardUiModel.Shape.entries.random(),
                        isPortraitMode = true,
                    )
                },
                isPortrait = true,
            )
        )
    }
}