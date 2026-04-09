// ui/components/EmptyState.kt
package com.example.smartbudget.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.smartbudget.ui.theme.*

@Composable
fun EmptyState(
    message: String = "Aucune dépense ce mois-ci",
    modifier: Modifier = Modifier
) {
    Box(
        modifier           = modifier.fillMaxSize(),
        contentAlignment   = Alignment.Center
    ) {
        Card(
            shape  = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = surfaceCard),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
            modifier = Modifier
                .padding(horizontal = 40.dp)
                .wrapContentSize()
        ) {
            Column(
                modifier              = Modifier.padding(36.dp),
                verticalArrangement   = Arrangement.Center,
                horizontalAlignment   = Alignment.CenterHorizontally
            ) {
                Text(
                    text  = "💸",
                    fontSize = 52.sp
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text      = message,
                    style     = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center,
                    color     = textSecondary
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text      = "Appuie sur + pour commencer",
                    style     = MaterialTheme.typography.bodySmall,
                    textAlign = TextAlign.Center,
                    color     = textSecondary.copy(alpha = 0.6f)
                )
            }
        }
    }
}