package com.team23.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.team23.ui.card.CardUiModel
import com.team23.ui.card.SetCard
import com.team23.ui.shape.FillingTypeUiModel
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App(greeting: String) {
    val cards = CardUiModel.Color.entries.toTypedArray().flatMap { color ->
        FillingTypeUiModel.entries.toTypedArray().flatMap { fillingType ->
            CardUiModel.Shape.entries.flatMap { shape ->
                (1..3).map { patternAmount ->
                    CardUiModel(
                        patternAmount = patternAmount,
                        color = color,
                        fillingType = fillingType,
                        shape = shape,
                        selected = false,
                        visible = true,
                        isPortraitMode = false,
                    )
                }
            }
        }
    }

    MaterialTheme {
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier
                .safeContentPadding()
                .fillMaxSize(),
        ) {
            stickyHeader {
                Text(greeting)
            }
            items(cards) { card ->
                SetCard(card)
            }
        }
    }
}