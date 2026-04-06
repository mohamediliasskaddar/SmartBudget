// ui/components/CategoryPicker.kt
package com.example.smartbudget.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.smartbudget.data.local.entity.CategoryEntity

@Composable
fun CategoryPicker(
    categories: List<CategoryEntity>,
    selectedId: Long?,
    onSelect: (Long?) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        item {
            CategoryChip(
                category = CategoryEntity(id = -1, name = "Tout", icon = "📋", color = "#607D8B"),
                isSelected = selectedId == null,
                onClick = { onSelect(null) }
            )
        }
        items(categories) { cat ->
            CategoryChip(
                category   = cat,
                isSelected = selectedId == cat.id,
                onClick    = { onSelect(cat.id) }
            )
        }
    }
}