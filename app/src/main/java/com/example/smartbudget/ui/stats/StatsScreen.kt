// ui/stats/StatsScreen.kt
package com.example.smartbudget.ui.stats

import android.graphics.Color
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.smartbudget.SmartBudgetApp
import com.example.smartbudget.data.model.CategoryStats
import com.example.smartbudget.ui.components.BackgroundImage
import com.example.smartbudget.ui.components.MonthNavigator
import com.example.smartbudget.ui.components.TotalCard
import com.example.smartbudget.ui.theme.*
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.PercentFormatter
import kotlin.collections.toIntArray

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

    var budgetDialogCat  by remember { mutableStateOf<CategoryStats?>(null) }
    var budgetInput      by remember { mutableStateOf("") }
    var budgetInputError by remember { mutableStateOf(false) }

    val sliceColors = ChartColors.asList.map { it.toArgb() }

    BackgroundImage(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {

            // ── Header zone bleue ───────────────────────────────
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .padding(top = 16.dp, bottom = 24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text       = "Statistiques",
                    fontSize   = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color      = white
                )

                MonthNavigator(
                    year       = state.year,
                    month      = state.month,
                    onPrevious = vm::previousMonth,
                    onNext     = vm::nextMonth
                )

                TotalCard(total = state.total)
            }

            // ── Contenu bas — surface blanche ───────────────────
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp))
                    .background(Brush.verticalGradient(
                        colors = listOf(surfaceCard, white)
                    ))
            ) {
                if (state.budgetRows.isEmpty()) {
                    // État vide
                    Box(
                        modifier         = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text("📊", fontSize = 48.sp)
                            Text(
                                "Aucune dépense ce mois-ci",
                                fontSize = 16.sp,
                                color    = textSecondary
                            )
                        }
                    }
                } else {
                    LazyColumn(
                        modifier        = Modifier.fillMaxSize(),
                        contentPadding  = PaddingValues(
                            start = 16.dp,
                            end = 16.dp,
                            top        = 24.dp,
                            bottom     = 32.dp
                        ),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {

                        // ── Top catégorie ───────────────────────
                        state.topCategory?.let { top ->
                            item {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(16.dp))
                                        .background(
                                            Brush.horizontalGradient(
                                                colors = listOf(indigoPrimary, blueAccent)
                                            )
                                        )
                                        .padding(horizontal = 16.dp, vertical = 14.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment     = Alignment.CenterVertically
                                    ) {
                                        Row(
                                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                                            verticalAlignment     = Alignment.CenterVertically
                                        ) {
                                            Text("🏆", fontSize = 22.sp)
                                            Column {
                                                Text(
                                                    "Top catégorie",
                                                    fontSize = 11.sp,
                                                    color    = textOnDarkSub
                                                )
                                                Text(
                                                    "${top.categoryIcon} ${top.categoryName}",
                                                    fontSize   = 15.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color      = white
                                                )
                                            }
                                        }
                                        Text(
                                            "%.2f MAD".format(top.total),
                                            fontSize   = 15.sp,
                                            fontWeight = FontWeight.Bold,
                                            color      = white
                                        )
                                    }
                                }
                            }
                        }

                        // ── Camembert ───────────────────────────
                        if (state.statsByCategory.isNotEmpty()) {
                            item {
                                Card(
                                    shape     = RoundedCornerShape(20.dp),
                                    colors    = CardDefaults.cardColors(
                                        containerColor = surfaceCard
                                    ),
                                    elevation = CardDefaults.cardElevation(0.dp),
                                    modifier  = Modifier.fillMaxWidth()
                                ) {
                                    Column(
                                        modifier = Modifier.padding(16.dp),
                                        verticalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Text(
                                            "Répartition",
                                            fontSize   = 15.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color      = textPrimary
                                        )
                                        AndroidView(
                                            factory = { ctx ->
                                                PieChart(ctx).apply {
                                                    layoutParams = ViewGroup.LayoutParams(
                                                        ViewGroup.LayoutParams.MATCH_PARENT, 600
                                                    )
                                                    description.isEnabled   = false
                                                    isDrawHoleEnabled       = true
                                                    holeRadius              = 54f
                                                    transparentCircleRadius = 58f
                                                    setHoleColor(Color.TRANSPARENT)
                                                    setUsePercentValues(true)
                                                    legend.isEnabled        = true
                                                    legend.textColor        = Color.parseColor("#4A4C7A")
                                                    legend.textSize         = 11f
                                                    setEntryLabelColor(Color.WHITE)
                                                    setEntryLabelTextSize(10f)
                                                }
                                            },
                                            update = { chart ->
                                                val entries = state.statsByCategory.map { s ->
                                                    PieEntry(
                                                        s.total.toFloat(),
                                                        "${s.categoryIcon} ${s.categoryName}"
                                                    )
                                                }
                                                val colors = state.statsByCategory.indices.map { i ->
                                                    sliceColors[i % sliceColors.size]
                                                }
                                                val dataSet = PieDataSet(entries, "").apply {
                                                    setColors(*colors.toIntArray())
                                                    valueTextColor = Color.WHITE
                                                    valueTextSize  = 11f
                                                    sliceSpace     = 2f
                                                }
                                                chart.data = PieData(dataSet).apply {
                                                    setValueFormatter(PercentFormatter(chart))
                                                }
                                                chart.invalidate()
                                            },
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(240.dp)
                                        )
                                    }
                                }
                            }
                        }

                        // ── Titre section budgets ────────────────
                        item {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment     = Alignment.CenterVertically
                            ) {
                                Text(
                                    "Détail & budgets",
                                    fontSize   = 15.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color      = textPrimary
                                )
                                Text(
                                    "✏️ pour définir un budget",
                                    fontSize = 11.sp,
                                    color    = textSecondary
                                )
                            }
                        }

                        // ── Rows budget ──────────────────────────
                        items(state.budgetRows) { row ->
                            val overBudget = row.budgetLimit != null && row.progress > 1f

                            Card(
                                shape     = RoundedCornerShape(16.dp),
                                colors    = CardDefaults.cardColors(
                                    containerColor = if (overBudget)
                                        errorRed.copy(alpha = 0.06f)
                                    else
                                        surfaceCard
                                ),
                                elevation = CardDefaults.cardElevation(0.dp),
                                modifier  = Modifier.fillMaxWidth()
                            ) {
                                Column(
                                    modifier = Modifier.padding(14.dp),
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment     = Alignment.CenterVertically
                                    ) {
                                        // Icône + nom
                                        Row(
                                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                                            verticalAlignment     = Alignment.CenterVertically
                                        ) {
                                            Box(
                                                modifier = Modifier
                                                    .size(40.dp)
                                                    .clip(RoundedCornerShape(12.dp))
                                                    .background(
                                                        indigoPrimary.copy(alpha = 0.08f)
                                                    ),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Text(
                                                    row.stats.categoryIcon,
                                                    fontSize = 18.sp
                                                )
                                            }
                                            Column {
                                                Text(
                                                    row.stats.categoryName,
                                                    fontSize   = 14.sp,
                                                    fontWeight = FontWeight.SemiBold,
                                                    color      = textPrimary
                                                )
                                                Text(
                                                    "${row.stats.count} dépense(s)",
                                                    fontSize = 11.sp,
                                                    color    = textSecondary
                                                )
                                            }
                                        }

                                        // Montant + bouton budget
                                        Row(
                                            verticalAlignment     = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                                        ) {
                                            Box(
                                                modifier = Modifier
                                                    .clip(RoundedCornerShape(8.dp))
                                                    .background(
                                                        if (overBudget)
                                                            errorRed.copy(alpha = 0.10f)
                                                        else
                                                            indigoPrimary.copy(alpha = 0.08f)
                                                    )
                                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                                            ) {
                                                Text(
                                                    "%.2f MAD".format(row.stats.total),
                                                    fontSize   = 13.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color      = if (overBudget) errorRed
                                                    else indigoPrimary
                                                )
                                            }
                                            IconButton(
                                                onClick = {
                                                    budgetDialogCat  = row.stats
                                                    budgetInput      = row.budgetLimit?.toString() ?: ""
                                                    budgetInputError = false
                                                },
                                                modifier = Modifier.size(32.dp)
                                            ) {
                                                Icon(
                                                    Icons.Default.Edit,
                                                    contentDescription = "Définir budget",
                                                    tint     = textSecondary,
                                                    modifier = Modifier.size(16.dp)
                                                )
                                            }
                                        }
                                    }

                                    // Barre progression
                                    if (row.budgetLimit != null) {
                                        val barColor = when {
                                            row.progress > 1f   -> errorRed
                                            row.progress > 0.8f -> warningAmber
                                            else                -> indigoPrimary
                                        }
                                        Column(
                                            verticalArrangement = Arrangement.spacedBy(4.dp)
                                        ) {
                                            LinearProgressIndicator(
                                                progress  = { row.progress.coerceIn(0f, 1f) },
                                                modifier  = Modifier
                                                    .fillMaxWidth()
                                                    .height(7.dp)
                                                    .clip(RoundedCornerShape(4.dp)),
                                                color      = barColor,
                                                trackColor = barColor.copy(alpha = 0.12f)
                                            )
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.SpaceBetween
                                            ) {
                                                Text(
                                                    if (overBudget) "⚠️ Dépassement !"
                                                    else "%.0f%%".format(row.progress * 100),
                                                    fontSize = 11.sp,
                                                    color    = if (overBudget) errorRed
                                                    else textSecondary,
                                                    fontWeight = if (overBudget) FontWeight.SemiBold
                                                    else FontWeight.Normal
                                                )
                                                Text(
                                                    "Limite : %.2f MAD".format(row.budgetLimit),
                                                    fontSize = 11.sp,
                                                    color    = textSecondary
                                                )
                                            }
                                        }
                                    } else {
                                        Text(
                                            "Aucun budget défini · appuie sur ✏️",
                                            fontSize = 11.sp,
                                            color    = textSecondary.copy(alpha = 0.6f)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // ── Dialog budget ──────────────────────────────────────────
    budgetDialogCat?.let { cat ->
        val existingBudget = state.budgets.find { it.categoryId == cat.categoryId }

        AlertDialog(
            onDismissRequest = { budgetDialogCat = null },
            shape            = RoundedCornerShape(20.dp),
            containerColor   = white,
            title = {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment     = Alignment.CenterVertically
                ) {
                    Text(cat.categoryIcon, fontSize = 20.sp)
                    Text(
                        cat.categoryName,
                        fontWeight = FontWeight.Bold,
                        color      = textPrimary
                    )
                }
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        "Limite mensuelle pour cette catégorie",
                        fontSize = 13.sp,
                        color    = textSecondary
                    )
                    OutlinedTextField(
                        value         = budgetInput,
                        onValueChange = { budgetInput = it; budgetInputError = false },
                        label         = { Text("Montant (MAD)") },
                        isError       = budgetInputError,
                        supportingText = {
                            if (budgetInputError) Text("Montant invalide", color = errorRed)
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Decimal
                        ),
                        singleLine = true,
                        shape      = RoundedCornerShape(12.dp),
                        colors     = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor   = indigoPrimary,
                            unfocusedBorderColor = indigoPrimary.copy(alpha = 0.25f),
                            focusedLabelColor    = indigoPrimary
                        )
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val amount = budgetInput.replace(",", ".").toDoubleOrNull()
                        if (amount == null || amount <= 0) {
                            budgetInputError = true
                            return@Button
                        }
                        vm.upsertBudget(cat.categoryId, amount)
                        budgetDialogCat = null
                    },
                    shape  = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = indigoPrimary,
                        contentColor   = white
                    )
                ) {
                    Text("Enregistrer", fontWeight = FontWeight.SemiBold)
                }
            },
            dismissButton = {
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    if (existingBudget != null) {
                        TextButton(
                            onClick = {
                                vm.deleteBudget(existingBudget)
                                budgetDialogCat = null
                            }
                        ) {
                            Text(
                                "Supprimer",
                                color      = errorRed,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                    TextButton(onClick = { budgetDialogCat = null }) {
                        Text("Annuler", color = textSecondary)
                    }
                }
            }
        )
    }
}