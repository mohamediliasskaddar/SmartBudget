// ui/navigation/BottomNavBar.kt
package com.example.smartbudget.ui.navigation

import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.smartbudget.R

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List

data class NavItem(val screen: Screen, val label: String, val icon: @Composable () -> Unit)

@Composable
fun BottomNavBar(navController: NavController) {
    val items = listOf(
        NavItem(Screen.Expenses, "Dépenses") {
            Icon(androidx.compose.material.icons.Icons.Default.List, contentDescription = null)
        },
        NavItem(Screen.Stats, "Stats") {
            Icon(androidx.compose.material.icons.Icons.Default.PieChart, contentDescription = null)
        },
        NavItem(Screen.Settings, "Paramètres") {
            Icon(androidx.compose.material.icons.Icons.Default.Settings, contentDescription = null)
        }
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar {
        items.forEach { item ->
            NavigationBarItem(
                icon    = item.icon,
                label   = { Text(item.label) },
                selected = currentRoute == item.screen.route,
                onClick  = {
                    navController.navigate(item.screen.route) {
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true
                        restoreState    = true
                    }
                }
            )
        }
    }
}