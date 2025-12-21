package com.team23.ui.settings

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.currentBackStackEntryAsState
import com.team23.ui.navigation.NavigationScreen

@Composable
fun SettingsFAB(
    navController: NavController,
    modifier: Modifier = Modifier,
) {
    val currentBackStack by navController.currentBackStackEntryAsState()
    val currentDestination = currentBackStack?.destination ?: return

    if (currentDestination.hasRoute<NavigationScreen.GameSelection>() ||
        currentDestination.hasRoute<NavigationScreen.Game>())  {
        FloatingActionButton(
            onClick = { navController.navigate(NavigationScreen.Settings.name) },
            modifier = modifier.size(56.dp),
        ) {
            Icon(
                imageVector = Icons.Outlined.Settings,
                contentDescription = "navigate to settings",
                modifier = Modifier.size(48.dp)
            )
        }
    }
}
