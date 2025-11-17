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
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material.icons.outlined.Memory
import androidx.compose.material.icons.outlined.Storage
import androidx.compose.material.icons.outlined.VideogameAsset
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.team23.ui.theming.LocalSpacings
import org.koin.compose.koinInject

@Composable
fun DebugManagementFAB(
    modifier: Modifier = Modifier,
) {
    val debugViewModel = koinInject<DebugViewModel>()

    DebugManagementFAB(
        onAction = debugViewModel::onAction,
        modifier = modifier
    )
}

@Composable
private fun DebugManagementFAB(
    modifier: Modifier = Modifier,
    onAction: (DebugAction) -> Unit,
) {
    var isExpanded by remember { mutableStateOf(false) }
    val rotation by animateFloatAsState(
        targetValue = if (isExpanded) -225f else 0f,
        animationSpec = getTween(),
    )

    Column(
        modifier = modifier
            .padding(all = LocalSpacings.current.largeIncreased),
        horizontalAlignment = Alignment.End,
    ) {
        AnimatedVisibility(
            visible = isExpanded,
            enter = fadeIn(getTween(delayMillis = 3 * ANIMATION_DELAY)),
            exit = fadeOut(getTween()) + shrinkVertically(getTween()),
        ) {
            RecipeFabRowButton(
                text = "Clear games from memory",
                icon = Icons.Outlined.VideogameAsset,
                onIconClick = { onAction(DebugAction.ClearGames) },
                modifier = Modifier.padding(bottom = 8.dp),
            )
        }
        AnimatedVisibility(
            visible = isExpanded,
            enter = fadeIn(getTween(delayMillis = 2 * ANIMATION_DELAY)),
            exit = fadeOut(getTween()) + shrinkVertically(getTween()),
        ) {
            RecipeFabRowButton(
                text = "Clear all server memory",
                icon = Icons.Outlined.Memory,
                onIconClick = { onAction(DebugAction.ClearMemory) },
                modifier = Modifier.padding(bottom = 8.dp),
            )
        }
        AnimatedVisibility(
            visible = isExpanded,
            enter = fadeIn(getTween(delayMillis = ANIMATION_DELAY)),
            exit = fadeOut(getTween()) + shrinkVertically(getTween()),
        ) {
            RecipeFabRowButton(
                text = "Clear all server database",
                icon = Icons.Outlined.Storage,
                onIconClick = { onAction(DebugAction.ClearDb) },
                modifier = Modifier.padding(bottom = 8.dp),
            )
        }
        FloatingActionButton(
            onClick = { isExpanded = !isExpanded },
            modifier = modifier.size(56.dp),
        ) {
            Icon(
                imageVector = Icons.Outlined.Clear,
                contentDescription = null,
                modifier = Modifier
                    .size(24.dp)
                    .rotate(rotation),
            )
        }
    }
}

@Composable
private fun RecipeFabRowButton(
    text: String,
    icon: ImageVector,
    onIconClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier,
    ) {
        Text(text = text)
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
