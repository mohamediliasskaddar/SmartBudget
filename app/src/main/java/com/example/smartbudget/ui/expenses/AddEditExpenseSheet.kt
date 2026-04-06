// ui/expenses/AddEditExpenseSheet.kt
package com.example.smartbudget.ui.expenses

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.smartbudget.data.local.entity.CategoryEntity
import com.example.smartbudget.data.local.entity.ExpenseEntity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditExpenseSheet(
    initial: ExpenseEntity?,
    categories: List<CategoryEntity>,
    onSave: (ExpenseEntity) -> Unit,
    onDismiss: () -> Unit
) {
    var amountText   by remember { mutableStateOf(initial?.amount?.toString() ?: "") }
    var note         by remember { mutableStateOf(initial?.note ?: "") }
    var selectedCat  by remember { mutableStateOf(categories.firstOrNull()?.id ?: 0L) }
    var expanded     by remember { mutableStateOf(false) }
    var amountError  by remember { mutableStateOf(false) }

    val selectedCatName = categories.find { it.id == selectedCat }?.let {
        "${it.icon} ${it.name}"
    } ?: "Choisir"

    ModalBottomSheet(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text  = if (initial == null) "Ajouter une dépense" else "Modifier",
                style = MaterialTheme.typography.titleLarge
            )

            // Montant
            OutlinedTextField(
                value = amountText,
                onValueChange = { amountText = it; amountError = false },
                label = { Text("Montant (MAD)") },
                isError = amountError,
                supportingText = { if (amountError) Text("Montant invalide") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.fillMaxWidth()
            )

            // Catégorie dropdown
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                OutlinedTextField(
                    value = selectedCatName,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Catégorie") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    categories.forEach { cat ->
                        DropdownMenuItem(
                            text    = { Text("${cat.icon} ${cat.name}") },
                            onClick = { selectedCat = cat.id; expanded = false }
                        )
                    }
                }
            }

            // Note
            OutlinedTextField(
                value = note,
                onValueChange = { note = it },
                label = { Text("Note (optionnel)") },
                modifier = Modifier.fillMaxWidth()
            )

            // Bouton Enregistrer
            Button(
                onClick = {
                    val amount = amountText.toDoubleOrNull()
                    if (amount == null || amount <= 0) { amountError = true; return@Button }
                    val entity = (initial ?: ExpenseEntity(
                        amount = 0.0, date = System.currentTimeMillis(), categoryId = selectedCat
                    )).copy(
                        amount     = amount,
                        categoryId = selectedCat,
                        note       = note,
                        updatedAt  = System.currentTimeMillis()
                    )
                    onSave(entity)
                    onDismiss()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Enregistrer")
            }
        }
    }
}