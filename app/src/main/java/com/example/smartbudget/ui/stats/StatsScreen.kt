// ui/stats/StatsScreen.kt
package com.example.smartbudget.ui.stats

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.smartbudget.SmartBudgetApp
import com.example.smartbudget.ui.components.MonthNavigator
import com.example.smartbudget.ui.components.TotalCard

@Composable
fun StatsScreen() {
    val app = LocalContext.current.applicationContext as SmartBudgetApp
    val vm: StatsViewModel = viewModel(
        factory = StatsViewModel.Factory(app.expenseRepository)
    )
    val state by vm.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        MonthNavigator(
            year       = state.year,
            month      = state.month,
            onPrevious = vm::previousMonth,
            onNext     = vm::nextMonth
        )

        TotalCard(total = state.total)

        if (state.statsByCategory.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = androidx.compose.ui.Alignment.Center
            ) {
                Text("Aucune dépense ce mois-ci",
                    color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        } else {
            Text(
                text  = "Répartition par catégorie",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )

            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(state.statsByCategory) { stat ->
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Text(stat.categoryIcon,
                                    style = MaterialTheme.typography.titleMedium)
                                Column {
                                    Text(stat.categoryName,
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Medium)
                                    Text("${stat.count} dépense(s)",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                            }
                            Text(
                                "%.2f MAD".format(stat.total),
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }
        }
    }
}