// ui/expenses/ExpensesScreen.kt
package com.example.smartbudget.ui.expenses

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.smartbudget.SmartBudgetApp
import com.example.smartbudget.data.local.entity.ExpenseEntity
import com.example.smartbudget.ui.components.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpensesScreen() {
    val app = LocalContext.current.applicationContext as SmartBudgetApp
    val vm: ExpensesViewModel = viewModel(
        factory = ExpensesViewModel.Factory(app.expenseRepository, app.categoryRepository)
    )
    val state      by vm.uiState.collectAsState()
    val categories by vm.categories.collectAsState()

    // null = fermé | ExpenseEntity vide = ajout | entity existante = édition
    var sheetTarget  by remember { mutableStateOf<ExpenseEntity?>(null) }
    var showSheet    by remember { mutableStateOf(false) }
    var deleteTarget by remember { mutableStateOf<ExpenseEntity?>(null) }

    val catMap = categories.associateBy { it.id }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("SmartBudget") })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                sheetTarget = null   // mode ajout
                showSheet   = true
            }) {
                Icon(Icons.Default.Add, contentDescription = "Ajouter une dépense")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Navigation mois
            MonthNavigator(
                year       = state.year,
                month      = state.month,
                onPrevious = vm::previousMonth,
                onNext     = vm::nextMonth,
                modifier   = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
            )

            // Total
            TotalCard(
                total    = state.total,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 8.dp)
            )

            // Filtre catégories
            CategoryPicker(
                categories = categories,
                selectedId = state.selectedCategoryId,
                onSelect   = vm::filterByCategory
            )

            // Hint tap/long-press
            if (state.expenses.isNotEmpty()) {
                Text(
                    text  = "Tap pour modifier · Appui long pour supprimer",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    modifier  = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                )
            }

            // Liste ou état vide
            if (state.expenses.isEmpty()) {
                EmptyState()
            } else {
                LazyColumn(
                    contentPadding      = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(state.expenses, key = { it.id }) { expense ->
                        val cat = catMap[expense.categoryId]
                        ExpenseItem(
                            expense      = expense,
                            categoryIcon = cat?.icon ?: "📦",
                            categoryName = cat?.name ?: "Autre",
                            onClick      = {
                                sheetTarget = expense   // mode édition
                                showSheet   = true
                            },
                            onLongClick  = { deleteTarget = expense }
                        )
                    }
                }
            }
        }
    }

    // Sheet ajout / édition
    if (showSheet) {
        AddEditExpenseSheet(
            initial    = sheetTarget,
            categories = categories,
            onSave     = { entity ->
                if (entity.id == 0L) vm.addExpense(entity)
                else                 vm.updateExpense(entity)
            },
            onDismiss  = { showSheet = false; sheetTarget = null }
        )
    }

    // Dialog suppression
    deleteTarget?.let { expense ->
        AlertDialog(
            onDismissRequest = { deleteTarget = null },
            title   = { Text("Supprimer cette dépense ?") },
            text    = {
                val cat = catMap[expense.categoryId]
                Text("${cat?.icon ?: ""} ${cat?.name ?: "Dépense"} · " +
                        "%.2f MAD".format(expense.amount))
            },
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