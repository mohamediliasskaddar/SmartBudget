// ui/navigation/AppNavigation.kt
package com.example.smartbudget.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.smartbudget.ui.expenses.ExpensesScreen
import com.example.smartbudget.ui.stats.StatsScreen
import com.example.smartbudget.ui.settings.SettingsScreen

sealed class Screen(val route: String) {
    object Expenses : Screen("expenses")
    object Stats    : Screen("stats")
    object Settings : Screen("settings")
}

@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Expenses.route,
        modifier = modifier
    ) {
        composable(Screen.Expenses.route) { ExpensesScreen() }
        composable(Screen.Stats.route)    { StatsScreen() }
        composable(Screen.Settings.route) { SettingsScreen() }
    }
}