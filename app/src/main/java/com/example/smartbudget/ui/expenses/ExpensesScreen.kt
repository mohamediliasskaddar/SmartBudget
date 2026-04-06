// ui/expenses/ExpensesScreen.kt  — VERSION COMPLÈTE (remplace le stub)
package com.example.smartbudget.ui.expenses

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.smartbudget.SmartBudgetApp
import com.example.smartbudget.ui.components.EmptyState
import com.example.smartbudget.ui.components.CategoryPicker
import com.example.smartbudget.ui.components.MonthNavigator
import com.example.smartbudget.ui.components.TotalCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpensesScreen() {
    val app = LocalContext.current.applicationContext as SmartBudgetApp
    val vm: ExpensesViewModel = viewModel(
        factory = ExpensesViewModel.Factory(app.expenseRepository, app.categoryRepository)
    )
    val state      by vm.uiState.collectAsState()
    val categories by vm.categories.collectAsState()

    var showSheet    by remember { mutableStateOf(false) }
    var editTarget   by remember { mutableStateOf<com.example.smartbudget.data.local.entity.ExpenseEntity?>(null) }
    var deleteTarget by remember { mutableStateOf<com.example.smartbudget.data.local.entity.ExpenseEntity?>(null) }

    // Carte catégorie rapide pour l'affichage de chaque item
    val catMap = categories.associateBy { it.id }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { editTarget = null; showSheet = true }) {
                Icon(Icons.Default.Add, contentDescription = "Ajouter")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            MonthNavigator(
                year      = state.year,
                month     = state.month,
                onPrevious = vm::previousMonth,
                onNext     = vm::nextMonth,
                modifier   = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )

            TotalCard(
                total    = state.total,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            CategoryPicker(
                categories = categories,
                selectedId = state.selectedCategoryId,
                onSelect   = vm::filterByCategory
            )

            Spacer(modifier = Modifier.height(8.dp))

            if (state.expenses.isEmpty()) {
                EmptyState()
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(state.expenses, key = { it.id }) { expense ->
                        val cat = catMap[expense.categoryId]
                        ExpenseItem(
                            expense      = expense,
                            categoryIcon = cat?.icon ?: "📦",
                            categoryName = cat?.name ?: "Autre",
                            onLongClick  = { deleteTarget = expense }
                        )
                    }
                }
            }
        }
    }

    // Bottom sheet ajout / édition
    if (showSheet) {
        AddEditExpenseSheet(
            initial    = editTarget,
            categories = categories,
            onSave     = { e -> if (e.id == 0L) vm.addExpense(e) else vm.updateExpense(e) },
            onDismiss  = { showSheet = false }
        )
    }

    // Dialog confirmation suppression
    deleteTarget?.let { expense ->
        AlertDialog(
            onDismissRequest = { deleteTarget = null },
            title   = { Text("Supprimer ?") },
            text    = { Text("Cette dépense sera supprimée définitivement.") },
            confirmButton = {
                TextButton(onClick = { vm.deleteExpense(expense); deleteTarget = null }) {
                    Text("Supprimer", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { deleteTarget = null }) { Text("Annuler") }
            }
        )
    }
}