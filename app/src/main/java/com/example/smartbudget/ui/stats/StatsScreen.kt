// ui/stats/StatsScreen.kt
package com.example.smartbudget.ui.stats

import android.graphics.Color
import android.view.ViewGroup
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.smartbudget.SmartBudgetApp
import com.example.smartbudget.ui.components.MonthNavigator
import com.example.smartbudget.ui.components.TotalCard
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.PercentFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatsScreen() {
    val context = LocalContext.current
    val app     = context.applicationContext as SmartBudgetApp
    val vm: StatsViewModel = viewModel(
        factory = StatsViewModel.Factory(
            app.expenseRepository,
            app.budgetRepository,
            app.categoryRepository
        )
    )
    val state by vm.uiState.collectAsState()

    // Dialog budget
    var budgetDialogCat by remember { mutableStateOf<com.example.smartbudget.data.model.CategoryStats?>(null) }
    var budgetInput     by remember { mutableStateOf("") }
    var budgetInputError by remember { mutableStateOf(false) }

    LazyColumn(
        modifier            = Modifier.fillMaxSize(),
        contentPadding      = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        // Navigation mois
        item {
            MonthNavigator(
                year       = state.year,
                month      = state.month,
                onPrevious = vm::previousMonth,
                onNext     = vm::nextMonth
            )
        }

        // Total
        item { TotalCard(total = state.total) }

        // Top catégorie
        state.topCategory?.let { top ->
            item {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment     = Alignment.CenterVertically
                    ) {
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically) {
                            Text("🏆", style = MaterialTheme.typography.titleLarge)
                            Column {
                                Text("Top catégorie",
                                    style = MaterialTheme.typography.labelMedium)
                                Text("${top.categoryIcon} ${top.categoryName}",
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontWeight = FontWeight.Bold)
                            }
                        }
                        Text("%.2f MAD".format(top.total),
                            style      = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // ── Camembert ──────────────────────────────────────────
        if (state.statsByCategory.isNotEmpty()) {
            item {
                Text("Répartition", style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold)

                val sliceColors = listOf(
                    Color.parseColor("#FF5722"), Color.parseColor("#2196F3"),
                    Color.parseColor("#4CAF50"), Color.parseColor("#FF9800"),
                    Color.parseColor("#9C27B0"), Color.parseColor("#00BCD4"),
                    Color.parseColor("#607D8B"), Color.parseColor("#E91E63")
                )

                AndroidView(
                    factory = { ctx ->
                        PieChart(ctx).apply {
                            layoutParams = ViewGroup.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT, 700
                            )
                            description.isEnabled = false
                            isDrawHoleEnabled      = true
                            holeRadius             = 52f
                            transparentCircleRadius = 57f
                            setHoleColor(Color.TRANSPARENT)
                            setUsePercentValues(true)
                            legend.isEnabled       = true
                            legend.textColor       = Color.GRAY
                            legend.textSize        = 11f
                            setEntryLabelColor(Color.WHITE)
                            setEntryLabelTextSize(11f)
                        }
                    },
                    update = { chart ->
                        val entries = state.statsByCategory.mapIndexed { i, s ->
                            PieEntry(s.total.toFloat(), "${s.categoryIcon} ${s.categoryName}")
                        }
                        val colors = state.statsByCategory.mapIndexed { i, _ ->
                            sliceColors[i % sliceColors.size]
                        }
                        val dataSet = PieDataSet(entries, "").apply {
                            setColors(colors)
                            valueTextColor = Color.WHITE
                            valueTextSize  = 12f
                            sliceSpace     = 2f
                        }
                        val data = PieData(dataSet).apply {
                            setValueFormatter(PercentFormatter(chart))
                        }
                        chart.data = data
                        chart.invalidate()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(280.dp)
                )
            }
        }

        // ── Détail par catégorie + budget ──────────────────────
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment     = Alignment.CenterVertically
            ) {
                Text("Détail & budgets",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold)
                Text("Appuie sur ✏️ pour définir un budget",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }

        if (state.budgetRows.isEmpty()) {
            item {
                Box(Modifier.fillMaxWidth().padding(32.dp),
                    contentAlignment = Alignment.Center) {
                    Text("Aucune dépense ce mois-ci",
                        color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }

        items(state.budgetRows) { row ->
            val overBudget = row.budgetLimit != null && row.progress > 1f
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors   = CardDefaults.cardColors(
                    containerColor = if (overBudget)
                        MaterialTheme.colorScheme.errorContainer
                    else
                        MaterialTheme.colorScheme.surface
                )
            ) {
                Column(modifier = Modifier.padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)) {

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment     = Alignment.CenterVertically
                    ) {
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically) {
                            Text(row.stats.categoryIcon,
                                style = MaterialTheme.typography.titleMedium)
                            Column {
                                Text(row.stats.categoryName,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Medium)
                                Text("${row.stats.count} dépense(s)",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                        Row(verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text("%.2f MAD".format(row.stats.total),
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = if (overBudget)
                                    MaterialTheme.colorScheme.error
                                else
                                    MaterialTheme.colorScheme.primary)
                            IconButton(
                                onClick = {
                                    budgetDialogCat = row.stats
                                    budgetInput     = row.budgetLimit?.toString() ?: ""
                                    budgetInputError = false
                                },
                                modifier = Modifier.size(32.dp)
                            ) {
                                Icon(Icons.Default.Edit,
                                    contentDescription = "Définir budget",
                                    modifier = Modifier.size(16.dp))
                            }
                        }
                    }

                    // Barre de progression budget
                    if (row.budgetLimit != null) {
                        val progressColor = when {
                            row.progress > 1f  -> MaterialTheme.colorScheme.error
                            row.progress > 0.8f -> MaterialTheme.colorScheme.tertiary
                            else               -> MaterialTheme.colorScheme.primary
                        }
                        LinearProgressIndicator(
                            progress          = { row.progress.coerceIn(0f, 1f) },
                            modifier          = Modifier
                                .fillMaxWidth()
                                .height(6.dp),
                            color             = progressColor,
                            trackColor        = MaterialTheme.colorScheme.surfaceVariant
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                if (overBudget) "⚠️ Dépassement !"
                                else "%.0f%%".format(row.progress * 100),
                                style = MaterialTheme.typography.labelSmall,
                                color = if (overBudget) MaterialTheme.colorScheme.error
                                else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text("Budget : %.2f MAD".format(row.budgetLimit),
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    } else {
                        Text("Aucun budget défini",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
        }
    }

    // ── Dialog définir budget ──────────────────────────────────
    budgetDialogCat?.let { cat ->
        val existingBudget = state.budgets.find { it.categoryId == cat.categoryId }

        AlertDialog(
            onDismissRequest = { budgetDialogCat = null },
            title = { Text("Budget — ${cat.categoryIcon} ${cat.categoryName}") },
            text  = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Définir une limite mensuelle pour cette catégorie.",
                        style = MaterialTheme.typography.bodySmall)
                    OutlinedTextField(
                        value   = budgetInput,
                        onValueChange = { budgetInput = it; budgetInputError = false },
                        label   = { Text("Montant limite (MAD)") },
                        isError = budgetInputError,
                        supportingText = {
                            if (budgetInputError) Text("Montant invalide")
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Decimal
                        ),
                        singleLine = true
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    val amount = budgetInput.replace(",", ".").toDoubleOrNull()
                    if (amount == null || amount <= 0) {
                        budgetInputError = true
                        return@TextButton
                    }
                    vm.upsertBudget(cat.categoryId, amount)
                    budgetDialogCat = null
                }) { Text("Enregistrer") }
            },
            dismissButton = {
                Row {
                    if (existingBudget != null) {
                        TextButton(onClick = {
                            vm.deleteBudget(existingBudget)
                            budgetDialogCat = null
                        }) {
                            Text("Supprimer", color = MaterialTheme.colorScheme.error)
                        }
                    }
                    TextButton(onClick = { budgetDialogCat = null }) {
                        Text("Annuler")
                    }
                }
            }
        )
    }
}