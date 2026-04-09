// ui/navigation/BottomNavBar.kt
package com.example.smartbudget.ui.navigation

import android.R.attr.maxLines
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.HelpCenter
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.smartbudget.ui.theme.indigoPrimary
import com.example.smartbudget.ui.theme.skyBlue
import com.example.smartbudget.ui.theme.white

data class NavItem(val screen: Screen, val label: String, val icon: @Composable () -> Unit)

@Composable
fun BottomNavBar(navController: NavController, onAddClick: () -> Unit) {
    val items = listOf(
        NavItem(Screen.Expenses, "Dépenses") {
            Icon(Icons.AutoMirrored.Default.List, contentDescription = null)
        },
        NavItem(Screen.Stats, "Stats") {
            Icon(Icons.Default.PieChart, contentDescription = null)
        },
        NavItem(Screen.Settings, "Paramètres") {
            Icon(Icons.Default.Settings, contentDescription = null)
        },
        NavItem(Screen.Guide, "Guide") {
            Icon(Icons.Default.HelpCenter, contentDescription = null)
        }
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(white)
            .background(Color.Blue) // 👈
    ) {
        NavigationBar(
            containerColor = white,
            contentColor = indigoPrimary,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Left side: 2 items
            Row(
                modifier = Modifier
                    .weight(0.5f)
                    .height(80.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)
            ) {
                repeat(2) { index ->
                    val item = items[index]
                    NavigationBarItem(
                        icon = item.icon,
                        label = {
                            Text(item.label,
                                fontSize = 10.sp
                            )},
                        selected = currentRoute == item.screen.route,
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = indigoPrimary,
                            selectedTextColor = indigoPrimary,
                            indicatorColor = indigoPrimary.copy(alpha = 0.7f),
                            unselectedIconColor = skyBlue,
                            unselectedTextColor = skyBlue
                        ),
                        onClick = {
                            navController.navigate(item.screen.route) {
                                popUpTo(navController.graph.startDestinationId) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }

            // Center: Add Button
            Box(
                modifier = Modifier
                    .weight(0.2f)
                    .height(60.dp),
                contentAlignment = Alignment.Center
            ) {
                FloatingActionButton(
                    onClick = onAddClick,
                    containerColor = indigoPrimary,
                    contentColor = white,
                    modifier = Modifier.size(60.dp), //56
                    shape = CircleShape
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Ajouter une dépense")
                }
            }

            // Right side: 2 items
            Row(
                modifier = Modifier
                    .weight(0.5f)
                    .height(80.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)
            ) {
                repeat(2) { index ->
                    val item = items[2 + index]
                    NavigationBarItem(
                        icon = item.icon,
                        label = {
                            Text(item.label,
                                fontSize = 10.sp
                            )},
                        selected = currentRoute == item.screen.route,
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = indigoPrimary,
                            selectedTextColor = indigoPrimary,
                            indicatorColor = indigoPrimary.copy(alpha = 0.7f),
                            unselectedIconColor = skyBlue,
                            unselectedTextColor = skyBlue
                        ),
                        onClick = {
                            navController.navigate(item.screen.route) {
                                popUpTo(navController.graph.startDestinationId) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BottomNavBarPreview() {
    val navController = androidx.navigation.compose.rememberNavController()

    // Fake wrapper to avoid crash in preview
    MaterialTheme {
        BottomNavBar(
            navController = navController,
            onAddClick = {}
        )
    }
}