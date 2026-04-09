package com.example.smartbudget

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.smartbudget.ui.navigation.AppNavHost
import com.example.smartbudget.ui.navigation.BottomNavBar
import com.example.smartbudget.ui.navigation.Screen
import com.example.smartbudget.ui.theme.SmartBudgetTheme
import com.example.smartbudget.data.local.entity.ExpenseEntity
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.platform.LocalContext
import com.example.smartbudget.ui.expenses.AddEditExpenseSheet
import com.example.smartbudget.ui.expenses.ExpensesViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SmartBudgetTheme {
                val navController = rememberNavController()

                // 🔥 Observe la route actuelle
                val currentBackStackEntry = navController.currentBackStackEntryAsState()
                val currentRoute = currentBackStackEntry.value?.destination?.route

                // Global state for Add/Edit Expense modal
                var showAddSheet by remember { mutableStateOf(false) }
                var sheetTarget by remember { mutableStateOf<ExpenseEntity?>(null) }

                // Get ExpenseViewModel for global access
                val app = LocalContext.current.applicationContext as SmartBudgetApp
                val vm: ExpensesViewModel = viewModel(
                    factory = ExpensesViewModel.Factory(app.expenseRepository, app.categoryRepository)
                )
                val categories by vm.categories.collectAsState()

                Scaffold(
                    bottomBar = {
                        // ❌ cacher sur Welcome
                        if (currentRoute != Screen.Welcome.route) {
                            BottomNavBar(
                                navController = navController,
                                onAddClick = {
                                    sheetTarget = null
                                    showAddSheet = true
                                }
                            )
                        }
                    }
                ) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        AppNavHost(
                            navController = navController,
                            modifier = Modifier
                        )
                    }

                    // Global Add/Edit Expense Modal Overlay
                    if (showAddSheet) {
                        AddEditExpenseSheet(
                            initial    = sheetTarget,
                            categories = categories,
                            onSave     = { entity ->
                                if (entity.id == 0L) vm.addExpense(entity)
                                else                 vm.updateExpense(entity)
                            },
                            onDismiss  = { showAddSheet = false; sheetTarget = null }
                        )
                    }
                }
            }
        }
    }
}