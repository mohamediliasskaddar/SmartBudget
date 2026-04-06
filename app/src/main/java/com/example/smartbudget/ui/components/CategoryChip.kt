// ui/components/CategoryChip.kt
package com.example.smartbudget.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.smartbudget.data.local.entity.CategoryEntity

@Composable
fun CategoryChip(
    category: CategoryEntity,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    FilterChip(
        selected = isSelected,
        onClick  = onClick,
        label    = { Text("${category.icon} ${category.name}") },
        modifier = modifier.padding(end = 6.dp)
    )
}