// ui/components/TotalCard.kt
package com.example.smartbudget.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.smartbudget.util.CurrencyUtils
import com.example.smartbudget.ui.theme.indigoPrimary
import com.example.smartbudget.ui.theme.white

@Composable
fun TotalCard(
    total: Double,
    modifier: Modifier = Modifier,
    currency: String = "MAD"
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = indigoPrimary,
            contentColor = white
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Total du mois",
                style = MaterialTheme.typography.labelMedium,
                color = white.copy(alpha = 0.8f)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = CurrencyUtils.format(total, currency),
                style = MaterialTheme.typography.headlineMedium,
                color = white
            )
        }
    }
}