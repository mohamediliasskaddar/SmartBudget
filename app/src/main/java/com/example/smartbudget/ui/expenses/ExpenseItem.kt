// ui/expenses/ExpenseItem.kt
package com.example.smartbudget.ui.expenses

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.smartbudget.data.local.entity.ExpenseEntity
import com.example.smartbudget.ui.theme.*
import com.example.smartbudget.util.CurrencyUtils
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ExpenseItem(
    expense: ExpenseEntity,
    categoryIcon: String,
    categoryName: String,
    onClick: () -> Unit,
    onLongClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val sdf = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())

    Card(
        modifier = modifier
            .fillMaxWidth()
            .combinedClickable(onClick = onClick, onLongClick = onLongClick),
        shape     = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        colors    = CardDefaults.cardColors(containerColor = surfaceCard)
    ) {
        Row(
            modifier          = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icône catégorie — cercle gradient
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(
                        Brush.linearGradient(
                            colors = listOf(indigoPrimary, blueAccent)
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(categoryIcon, fontSize = 20.sp)
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Infos
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text       = categoryName,
                    fontSize   = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color      = textPrimary
                )
                if (expense.note.isNotBlank()) {
                    Text(
                        text     = expense.note,
                        fontSize = 12.sp,
                        color    = textSecondary,
                        maxLines = 1
                    )
                }
                Text(
                    text     = sdf.format(Date(expense.date)),
                    fontSize = 11.sp,
                    color    = textSecondary.copy(alpha = 0.7f)
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Montant — badge indigo
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .background(indigoPrimary.copy(alpha = 0.08f))
                    .padding(horizontal = 10.dp, vertical = 5.dp)
            ) {
                Text(
                    text       = CurrencyUtils.format(expense.amount, expense.currency),
                    fontSize   = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color      = indigoPrimary
                )
            }
        }
    }
}