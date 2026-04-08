// ui/expenses/AddEditExpenseSheet.kt
package com.example.smartbudget.ui.expenses

import android.app.DatePickerDialog
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.smartbudget.data.local.entity.CategoryEntity
import com.example.smartbudget.data.local.entity.ExpenseEntity
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditExpenseSheet(
    initial: ExpenseEntity?,
    categories: List<CategoryEntity>,
    onSave: (ExpenseEntity) -> Unit,
    onDismiss: () -> Unit
) {
    val isEditing = initial != null

    var amountText  by remember { mutableStateOf(initial?.amount?.toString() ?: "") }
    var note        by remember { mutableStateOf(initial?.note ?: "") }
    var selectedCat by remember {
        mutableStateOf(initial?.categoryId ?: categories.firstOrNull()?.id ?: 0L)
    }
    var selectedDate by remember {
        mutableStateOf(initial?.date ?: System.currentTimeMillis())
    }
    var expanded    by remember { mutableStateOf(false) }
    var amountError by remember { mutableStateOf(false) }
    var catError    by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    val calendar = Calendar.getInstance().apply { timeInMillis = selectedDate }
    val datePickerDialog = DatePickerDialog(
        context,
        { _, y, m, d ->
            Calendar.getInstance().apply {
                set(y, m, d, 12, 0, 0)
                set(Calendar.MILLISECOND, 0)
                selectedDate = timeInMillis
            }
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    val selectedCatLabel = categories.find { it.id == selectedCat }
        ?.let { "${it.icon} ${it.name}" } ?: "Choisir une catégorie"

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp)
                .padding(bottom = 40.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Text(
                text  = if (isEditing) "Modifier la dépense" else "Ajouter une dépense",
                style = MaterialTheme.typography.titleLarge
            )

            // Montant
            OutlinedTextField(
                value   = amountText,
                onValueChange = { amountText = it; amountError = false },
                label   = { Text("Montant (MAD)") },
                isError = amountError,
                supportingText = { if (amountError) Text("Montant invalide ou nul") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            // Catégorie
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                OutlinedTextField(
                    value    = selectedCatLabel,
                    onValueChange = {},
                    readOnly = true,
                    label    = { Text("Catégorie") },
                    isError  = catError,
                    supportingText = { if (catError) Text("Catégorie obligatoire") },
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
                            onClick = {
                                selectedCat = cat.id
                                expanded    = false
                                catError    = false
                            }
                        )
                    }
                }
            }

            // Date
            OutlinedTextField(
                value    = sdf.format(Date(selectedDate)),
                onValueChange = {},
                readOnly = true,
                label    = { Text("Date") },
                trailingIcon = {
                    IconButton(onClick = { datePickerDialog.show() }) {
                        Icon(Icons.Default.CalendarToday, contentDescription = "Choisir date")
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )

            // Note
            OutlinedTextField(
                value   = note,
                onValueChange = { note = it },
                label   = { Text("Note (optionnel)") },
                maxLines = 2,
                modifier = Modifier.fillMaxWidth()
            )
// Après le champ Note, avant le bouton :

// Méthode de paiement
            var paymentExpanded by remember { mutableStateOf(false) }
            val paymentOptions  = listOf("CASH" to "💵 Espèces", "CARD" to "💳 Carte", "TRANSFER" to "🏦 Virement")
            var selectedPayment by remember { mutableStateOf(initial?.paymentMethod ?: "CASH") }

            ExposedDropdownMenuBox(
                expanded = paymentExpanded,
                onExpandedChange = { paymentExpanded = !paymentExpanded }
            ) {
                OutlinedTextField(
                    value    = paymentOptions.find { it.first == selectedPayment }?.second ?: "💵 Espèces",
                    onValueChange = {},
                    readOnly = true,
                    label    = { Text("Méthode de paiement") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(paymentExpanded) },
                    modifier = Modifier.fillMaxWidth().menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = paymentExpanded,
                    onDismissRequest = { paymentExpanded = false }
                ) {
                    paymentOptions.forEach { (key, label) ->
                        DropdownMenuItem(
                            text    = { Text(label) },
                            onClick = { selectedPayment = key; paymentExpanded = false }
                        )
                    }
                }
            }

// Toggle dépense récurrente
            var isRecurring by remember { mutableStateOf(initial?.isRecurring ?: false) }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment     = Alignment.CenterVertically
            ) {
                Column {
                    Text("Dépense récurrente", style = MaterialTheme.typography.bodyMedium)
                    Text("Se répète chaque mois", style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                Switch(checked = isRecurring, onCheckedChange = { isRecurring = it })
            }
            // Bouton
            Button(
                onClick = {
                    val amount = amountText.replace(",", ".").toDoubleOrNull()
                    var valid  = true
                    if (amount == null || amount <= 0) { amountError = true; valid = false }
                    if (selectedCat <= 0)              { catError    = true; valid = false }
                    if (!valid) return@Button

                    val entity = if (isEditing) {
                        initial!!.copy(
                            amount     = amount!!,
                            categoryId = selectedCat,
                            date       = selectedDate,
                            note       = note.trim(),
                            updatedAt  = System.currentTimeMillis(),
                            paymentMethod = selectedPayment,
                            isRecurring = isRecurring
                        )
                    } else {
                        ExpenseEntity(
                            amount        = amount!!,
                            categoryId    = selectedCat,
                            date          = selectedDate,
                            note          = note.trim(),
                            currency      = "MAD",
                            paymentMethod = "CASH"
                        )
                    }
                    onSave(entity)
                    onDismiss()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (isEditing) "Enregistrer les modifications" else "Ajouter")
            }

            if (isEditing) {
                OutlinedButton(
                    onClick  = onDismiss,
                    modifier = Modifier.fillMaxWidth()
                ) { Text("Annuler") }
            }
        }
    }
}