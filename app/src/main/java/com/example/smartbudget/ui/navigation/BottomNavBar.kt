// ui/navigation/BottomNavBar.kt
package com.example.smartbudget.ui.navigation

import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import com.example.smartbudget.ui.theme.indigoPrimary
import com.example.smartbudget.ui.theme.skyBlue
import com.example.smartbudget.ui.theme.white

data class NavItem(val screen: Screen, val label: String, val icon: @Composable () -> Unit)

@Composable
fun BottomNavBar(navController: NavController) {
    val items = listOf(
        NavItem(Screen.Expenses, "Dépenses") {
            Icon(Icons.AutoMirrored.Default.List, contentDescription = null)
        },
        NavItem(Screen.Stats, "Stats") {
            Icon(Icons.Default.PieChart, contentDescription = null)
        },
        NavItem(Screen.Settings, "Paramètres") {
            Icon(Icons.Default.Settings, contentDescription = null)
        }
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar(
        containerColor = white,
        contentColor = indigoPrimary
    ) {
        items.forEach { item ->
            NavigationBarItem(
                icon    = item.icon,
                label   = { Text(item.label) },
                selected = currentRoute == item.screen.route,
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = indigoPrimary,
                    selectedTextColor = indigoPrimary,
                    indicatorColor = indigoPrimary.copy(alpha = 0.7f),
                    unselectedIconColor = skyBlue,
                    unselectedTextColor = skyBlue
                ),
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