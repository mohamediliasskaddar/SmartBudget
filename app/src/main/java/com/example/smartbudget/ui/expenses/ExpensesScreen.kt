// ui/expenses/ExpensesScreen.kt
package com.example.smartbudget.ui.expenses

import android.R.attr.divider
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.smartbudget.SmartBudgetApp
import com.example.smartbudget.data.local.entity.ExpenseEntity
import com.example.smartbudget.ui.components.*
import com.example.smartbudget.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpensesScreen() {
    val app = LocalContext.current.applicationContext as SmartBudgetApp
    val vm: ExpensesViewModel = viewModel(
        factory = ExpensesViewModel.Factory(app.expenseRepository, app.categoryRepository)
    )
    val state      by vm.uiState.collectAsState()
    val categories by vm.categories.collectAsState()

    var sheetTarget  by remember { mutableStateOf<ExpenseEntity?>(null) }
    var showSheet    by remember { mutableStateOf(false) }
    var deleteTarget by remember { mutableStateOf<ExpenseEntity?>(null) }

    val catMap = categories.associateBy { it.id }

    BackgroundImage(modifier = Modifier.fillMaxSize()) {

        Scaffold(
            containerColor = androidx.compose.ui.graphics.Color.Transparent,
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {

                // ── Header zone (sur le bg bleu) ───────────────
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .padding(top = 16.dp, bottom = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Titre
                    Text(
                        text       = "Mes dépenses",
                        fontSize   = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color      = white
                    )

                    // Navigation mois
                    MonthNavigator(
                        year       = state.year,
                        month      = state.month,
                        onPrevious = vm::previousMonth,
                        onNext     = vm::nextMonth
                    )

                    // Total card
                    TotalCard(total = state.total)
                }

                // ── Contenu bas — surface blanche arrondie ──────
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp))
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    surfaceCard,
                                    white
                                )
                            )
                        )
                ) {
                    Column(modifier = Modifier.fillMaxSize()) {

                        // Handle visuel
                        Box(
                            modifier = Modifier
                                .padding(top = 12.dp)
                                .size(width = 40.dp, height = 4.dp)
                                .clip(RoundedCornerShape(2.dp))
                                .background(textSecondary.copy(alpha = 0.3f))//to reviwe
                                .align(Alignment.CenterHorizontally)
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // Filtres catégorie
                        CategoryPicker(
                            categories = categories,
                            selectedId = state.selectedCategoryId,
                            onSelect   = vm::filterByCategory,
                            modifier   = Modifier.fillMaxWidth()
                        )

                        // Hint
                        if (state.expenses.isNotEmpty()) {
                            Text(
                                text      = "Tap pour modifier · Appui long pour supprimer",
                                fontSize  = 11.sp,
                                color     = textSecondary.copy(alpha = 0.6f),
                                textAlign = TextAlign.Center,
                                modifier  = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 6.dp, bottom = 2.dp)
                            )
                        }

                        // Liste / empty
                        if (state.expenses.isEmpty()) {
                            EmptyState()
                        } else {
                            LazyColumn(
                                contentPadding      = PaddingValues(
                                    start = 16.dp,
                                    end = 16.dp,
                                    top        = 8.dp,
                                    bottom     = 80.dp   // espace FAB
                                ),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                items(state.expenses, key = { it.id }) { expense ->
                                    val cat = catMap[expense.categoryId]
                                    ExpenseItem(
                                        expense      = expense,
                                        categoryIcon = cat?.icon ?: "📦",
                                        categoryName = cat?.name ?: "Autre",
                                        onClick      = {
                                            sheetTarget = expense
                                            showSheet   = true
                                        },
                                        onLongClick  = { deleteTarget = expense }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Dialog suppression
    deleteTarget?.let { expense ->
        val cat = catMap[expense.categoryId]
        AlertDialog(
            onDismissRequest = { deleteTarget = null },
            shape            = RoundedCornerShape(20.dp),
            containerColor   = white,
            title = {
                Text(
                    "Supprimer ?",
                    fontWeight = FontWeight.Bold,
                    color      = textPrimary
                )
            },
            text = {
                Text(
                    "${cat?.icon ?: ""} ${cat?.name ?: "Dépense"} · %.2f MAD".format(expense.amount),
                    color = textSecondary
                )
            },
            confirmButton = {
                TextButton(onClick = { vm.deleteExpense(expense); deleteTarget = null }) {
                    Text("Supprimer", color = errorRed, fontWeight = FontWeight.SemiBold)
                }
            },
            dismissButton = {
                TextButton(onClick = { deleteTarget = null }) {
                    Text("Annuler", color = textSecondary)
                }
            }
        )
    }
}
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ExpensesScreenPreview() {
    // Mock categories
    val mockCategories = listOf(
        com.example.smartbudget.data.local.entity.CategoryEntity(
            id = 1, name = "Alimentation", icon = "🍔", isActive = true , color = "#FF5722"),
        com.example.smartbudget.data.local.entity.CategoryEntity(
            id = 2, name = "Transport", icon = "🚌", isActive = true, color = "#4CAF50"
        ),
        com.example.smartbudget.data.local.entity.CategoryEntity(
            id = 3, name = "Loisirs", icon = "🎮", isActive = false, color = "#2196F3"
        )
    )

    // Mock expenses
    val mockExpenses = listOf(
        com.example.smartbudget.data.local.entity.ExpenseEntity(
            id = 1, categoryId = 1, amount = 150.0, note = "Déjeuner", date = System.currentTimeMillis()
        ),
        com.example.smartbudget.data.local.entity.ExpenseEntity(
            id = 2, categoryId = 2, amount = 40.0, note = "Taxi", date = System.currentTimeMillis()
        )
    )

    // Mock state
    val mockState = object {
        val year = 2026
        val month = 4
        val total = 190.0
        val selectedCategoryId: Long? = null
        val expenses = mockExpenses
    }

    // Just display the screen with mock data
    BackgroundImage(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {

            // Header
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .padding(top = 16.dp, bottom = 20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text("Mes dépenses", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = white)
                MonthNavigator(year = mockState.year, month = mockState.month, onPrevious = {}, onNext = {})
                TotalCard(total = mockState.total)
            }

            // Contenu blanc
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp))
                    .background(
                        Brush.verticalGradient(colors = listOf(surfaceCard, white))
                    )
            ) {
                Column(modifier = Modifier.fillMaxSize()) {

                    Box(
                        modifier = Modifier
                            .padding(top = 12.dp)
                            .size(width = 40.dp, height = 4.dp)
                            .clip(RoundedCornerShape(2.dp))
                            .background(textSecondary.copy(alpha = 0.3f))
                            .align(Alignment.CenterHorizontally)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    CategoryPicker(
                        categories = mockCategories,
                        selectedId = mockState.selectedCategoryId,
                        onSelect = {}
                    )

                    if (mockState.expenses.isEmpty()) {
                        EmptyState()
                    } else {
                        LazyColumn(
                            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 80.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(mockState.expenses) { expense ->
                                val cat = mockCategories.find { it.id == expense.categoryId }
                                ExpenseItem(
                                    expense = expense,
                                    categoryIcon = cat?.icon ?: "📦",
                                    categoryName = cat?.name ?: "Autre",
                                    onClick = {},
                                    onLongClick = {}
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}