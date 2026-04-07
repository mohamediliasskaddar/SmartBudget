// ui/expenses/ExpenseItem.kt
package com.example.smartbudget.ui.expenses

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.smartbudget.data.local.entity.ExpenseEntity
import com.example.smartbudget.util.CurrencyUtils
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ExpenseItem(
    expense: ExpenseEntity,
    categoryIcon: String,
    categoryName: String,
    onClick: () -> Unit,          // ouvre le sheet d'édition
    onLongClick: () -> Unit,      // ouvre la confirmation de suppression
    modifier: Modifier = Modifier
) {
    val sdf = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())

    Card(
        modifier = modifier
            .fillMaxWidth()
            .combinedClickable(
                onClick     = onClick,
                onLongClick = onLongClick
            ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icône catégorie
            Surface(
                shape = MaterialTheme.shapes.small,
                color = MaterialTheme.colorScheme.secondaryContainer,
                modifier = Modifier.size(44.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(categoryIcon, style = MaterialTheme.typography.titleMedium)
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Nom catégorie + note
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text       = categoryName,
                    style      = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium
                )
                if (expense.note.isNotBlank()) {
                    Text(
                        text  = expense.note,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1
                    )
                }
                Text(
                    text  = sdf.format(Date(expense.date)),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Montant
            Text(
                text       = CurrencyUtils.format(expense.amount, expense.currency),
                style      = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                color      = MaterialTheme.colorScheme.primary
            )
        }
    }
}