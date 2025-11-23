package com.team23.ui.debug

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.LogoDev
import androidx.compose.material.icons.outlined.Memory
import androidx.compose.material.icons.outlined.Storage
import androidx.compose.material.icons.outlined.VideogameAsset
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.team23.ui.theming.LocalSpacings
import com.team23.ui.theming.SetTheme
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.jetbrains.compose.ui.tooling.preview.PreviewParameter
import org.jetbrains.compose.ui.tooling.preview.PreviewParameterProvider

@Composable
fun DebugManagementFAB(
    modifier: Modifier = Modifier,
    isExpanded: MutableState<Boolean> = remember { mutableStateOf(false) },
    isLoading: Boolean = false,
    onAction: (DebugAction) -> Unit,
) {
    val rotation by animateFloatAsState(
        targetValue = if (isExpanded.value) -360f else 0f,
        animationSpec = getTween(),
    )

    Column(
        modifier = modifier
            .padding(all = LocalSpacings.current.largeIncreased),
        horizontalAlignment = Alignment.End,
    ) {
        AnimatedVisibility(
            visible = isExpanded.value,
            enter = fadeIn(getTween(delayMillis = 3 * ANIMATION_DELAY)),
            exit = fadeOut(getTween()) + shrinkVertically(getTween()),
        ) {
            DebugRowAction(
                text = "Clear games from memory",
                icon = Icons.Outlined.VideogameAsset,
                onIconClick = { onAction(DebugAction.ClearGames) },
                modifier = Modifier.padding(bottom = 8.dp),
            )
        }
        AnimatedVisibility(
            visible = isExpanded.value,
            enter = fadeIn(getTween(delayMillis = 2 * ANIMATION_DELAY)),
            exit = fadeOut(getTween()) + shrinkVertically(getTween()),
        ) {
            DebugRowAction(
                text = "Clear all server memory",
                icon = Icons.Outlined.Memory,
                onIconClick = { onAction(DebugAction.ClearMemory) },
                modifier = Modifier.padding(bottom = 8.dp),
            )
        }
        AnimatedVisibility(
            visible = isExpanded.value,
            enter = fadeIn(getTween(delayMillis = ANIMATION_DELAY)),
            exit = fadeOut(getTween()) + shrinkVertically(getTween()),
        ) {
            DebugRowAction(
                text = "Clear all server database",
                icon = Icons.Outlined.Storage,
                onIconClick = { onAction(DebugAction.ClearDb) },
                modifier = Modifier.padding(bottom = 8.dp),
            )
        }
        FloatingActionButton(
            onClick = { isExpanded.value = !isExpanded.value },
            modifier = modifier.size(56.dp),
        ) {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.size(48.dp))
            } else {
                Icon(
                    imageVector = Icons.Outlined.LogoDev,
                    contentDescription = null,
                    modifier = Modifier
                        .size(48.dp)
                        .rotate(rotation),
                )
            }
        }
    }
}

@Composable
private fun DebugRowAction(
    text: String,
    icon: ImageVector,
    onIconClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier,
    ) {
        Text(
            text = text,
            color = MaterialTheme.colorScheme.onBackground,
        )
        FloatingActionButton(
            onClick = onIconClick,
            modifier = Modifier
                .padding(horizontal = 12.dp)
                .size(32.dp),
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
            )
        }
    }
}

private const val ANIMATION_DELAY = 300

private fun <T> getTween(delayMillis: Int = 0) = tween<T>(
    durationMillis = 500,
    delayMillis = delayMillis,
)

@Composable
@Preview(showBackground = true, backgroundColor = 0xFFFFFFF)
private fun DebugManagementFABPreview(
    @PreviewParameter(BooleanParameterPreviewProvider::class) data: Pair<Boolean, Boolean>,
) {
    SetTheme {
        DebugManagementFAB(
            isExpanded = remember { mutableStateOf(data.first) },
            isLoading = data.second,
        ) { }
    }
}

private class BooleanParameterPreviewProvider : PreviewParameterProvider<Pair<Boolean, Boolean>> {
    override val values: Sequence<Pair<Boolean, Boolean>> = sequenceOf(
        true to true,
        true to false,
        false to false,
        false to true,
    )
}
