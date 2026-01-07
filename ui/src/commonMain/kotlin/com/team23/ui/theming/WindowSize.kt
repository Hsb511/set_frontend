package com.team23.ui.theming

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

// Inspired by https://developer.android.com/develop/ui/views/layout/use-window-size-classes
enum class WindowSize(
    private val minWidth: Dp = minimalWidthThreshold,
    private val maxWidth: Dp = maximalWidthThreshold,
    private val minHeight: Dp = minimalHeightThreshold,
    private val maxHeight: Dp = maximalHeightThreshold,
) {
    PhoneInPortrait(
        maxWidth = compactMediumWidthThreshold,
        minHeight = compactMediumHeightThreshold,
        maxHeight = mediumLargeHeightThreshold,
    ),

    PhoneInLandscape(
        maxHeight = compactMediumHeightThreshold,
    ),

    TabletInPortrait(
        minWidth = compactMediumWidthThreshold,
        maxWidth = mediumExpandedWidthThreshold,
        maxHeight = mediumLargeHeightThreshold,
    ),

    TabletInLandscape(
        minWidth = mediumExpandedWidthThreshold,
        maxWidth = expandedLargeWidthThreshold,
        minHeight = compactMediumHeightThreshold,
        maxHeight = mediumLargeHeightThreshold,
    ),

    DesktopInLandscape(
        minWidth = largeExtraLargeWidthThreshold,
    ),

    DesktopInPortrait(
        minHeight = mediumLargeHeightThreshold,
    );

    fun matches(width: Dp, height: Dp): Boolean =
        width in minWidth..<maxWidth && height in minHeight..<maxHeight

    @Composable
    fun getHorizontalGutter(): Dp = when(this) {
        PhoneInPortrait -> LocalSpacings.current.large
        PhoneInLandscape -> LocalSpacings.current.largeIncreased
        TabletInPortrait -> LocalSpacings.current.extraLarge
        TabletInLandscape -> LocalSpacings.current.extraLargeIncreased
        DesktopInLandscape -> LocalSpacings.current.extraLargeIncreased
        DesktopInPortrait -> LocalSpacings.current.jumbo
    }

    @Composable
    fun getVerticalGutter(): Dp = when(this) {
        PhoneInPortrait -> LocalSpacings.current.large
        PhoneInLandscape -> LocalSpacings.current.medium
        TabletInPortrait -> LocalSpacings.current.extraLargeIncreased
        TabletInLandscape -> LocalSpacings.current.extraLarge
        DesktopInLandscape -> LocalSpacings.current.jumbo
        DesktopInPortrait -> LocalSpacings.current.extraLargeIncreased
    }
}

@Composable
fun rememberWindowSize(): WindowSize {
    val containerSize = LocalWindowInfo.current.containerSize
    val density = LocalDensity.current
    val (windowWidth, windowHeight) = with(density) { containerSize.width.toDp() to containerSize.height.toDp() }

    return WindowSize.entries.firstOrNull { it.matches(windowWidth, windowHeight) } ?: WindowSize.DesktopInLandscape
}

// Width thresholds
private val minimalWidthThreshold = 0.dp
private val compactMediumWidthThreshold = 600.dp
private val mediumExpandedWidthThreshold = 840.dp
private val expandedLargeWidthThreshold = 1200.dp
private val largeExtraLargeWidthThreshold = 1600.dp
private val maximalWidthThreshold = Int.MAX_VALUE.dp

// Height thresholds
private val minimalHeightThreshold = 0.dp
private val compactMediumHeightThreshold = 480.dp
private val mediumLargeHeightThreshold = 900.dp
private val maximalHeightThreshold = Int.MAX_VALUE.dp
