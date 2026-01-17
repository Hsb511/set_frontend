package com.team23.ui.splash

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.unit.dp
import com.team23.ui.card.SetCard
import com.team23.ui.card.Slot
import com.team23.ui.card.Slot.CardUiModel.Color
import com.team23.ui.debug.showSplashScreen
import com.team23.ui.shape.FillingTypeUiModel
import com.team23.ui.theming.SetTheme
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject

@Composable
fun SplashScreen() {
    val splashViewModel = koinInject<SplashViewModel>()

    DisposableEffect(Unit) {
        splashViewModel.start()
        onDispose { }
    }

    if (showSplashScreen()) {
        val card by remember { mutableStateOf(splashViewModel.getRandomCard()) }

        SplashScreen(card)
    }
}

@Composable
fun SplashScreen(card: Slot.CardUiModel) {
    val infiniteTransition = rememberInfiniteTransition()
    val scale by infiniteTransition.animateFloat(
        initialValue = 0.9f,
        targetValue = 1.1f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1000,
                easing = LinearEasing,
            ),
            repeatMode = RepeatMode.Reverse
        )
    )

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        SetCard(
            card = card,
            modifier = Modifier
                .width(230.dp)
                .aspectRatio(16 / 9f)
                .scale(scale),
        )
    }
}

@Composable
@Preview
private fun SplashScreenPreview() {
    SetTheme {
        SplashScreen(
            card = Slot.CardUiModel(
                isPortraitMode = false,
                patternAmount = 2,
                color = Color.Primary,
                fillingType = FillingTypeUiModel.Filled,
                shape = Slot.CardUiModel.Shape.Squiggle,
            )
        )
    }
}
