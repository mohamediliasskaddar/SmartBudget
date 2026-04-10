// ui/components/CategoryChip.kt
package com.example.smartbudget.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.smartbudget.data.local.entity.CategoryEntity
import com.example.smartbudget.ui.theme.*

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
        label    = {
            Text(
                text       = "${category.icon} ${category.name}",
                fontSize   = 13.sp,
                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
            )
        },
        modifier = modifier.padding(end = 6.dp),
        colors   = FilterChipDefaults.filterChipColors(
            selectedContainerColor  = indigoPrimary,
            selectedLabelColor      = white,
            containerColor          = surfaceGlass,
            labelColor              = Color.Gray
        ),
        border = FilterChipDefaults.filterChipBorder(
            enabled              = true,
            selected             = isSelected,
            borderColor          = white.copy(alpha = 0.30f),
            selectedBorderColor  = indigoPrimary,
            borderWidth          = 1.dp,
            selectedBorderWidth  = 0.dp
        )
    )
}