package com.team23.ui.gameSelection

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.People
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.ui.graphics.vector.ImageVector
import kotlinx.serialization.Serializable

@Serializable
enum class MultiGameMode {
    TimeTrial,
    Versus,
}

val MultiGameMode.icon: ImageVector
    get() = when (this) {
        MultiGameMode.TimeTrial -> Icons.Outlined.Timer
        MultiGameMode.Versus -> Icons.Outlined.People
    }
