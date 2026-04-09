// ui/expenses/AddEditExpenseSheet.kt
package com.example.smartbudget.ui.expenses

import android.app.DatePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.smartbudget.data.local.entity.CategoryEntity
import com.example.smartbudget.data.local.entity.ExpenseEntity
import com.example.smartbudget.ui.theme.*
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

    var amountText   by remember { mutableStateOf(initial?.amount?.toString() ?: "") }
    var note         by remember { mutableStateOf(initial?.note ?: "") }
    var selectedCat  by remember {
        mutableStateOf(initial?.categoryId ?: categories.firstOrNull()?.id ?: 0L)
    }
    var selectedDate by remember {
        mutableStateOf(initial?.date ?: System.currentTimeMillis())
    }
    var catExpanded     by remember { mutableStateOf(false) }
    var payExpanded     by remember { mutableStateOf(false) }
    var amountError     by remember { mutableStateOf(false) }
    var catError        by remember { mutableStateOf(false) }
    var selectedPayment by remember { mutableStateOf(initial?.paymentMethod ?: "CASH") }
    var isRecurring     by remember { mutableStateOf(initial?.isRecurring ?: false) }

    val context = LocalContext.current
    val sdf     = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
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

    val selectedCatObj   = categories.find { it.id == selectedCat }
    val selectedCatLabel = selectedCatObj?.let { "${it.icon} ${it.name}" } ?: "Choisir une catégorie"

    val paymentOptions = listOf(
        "CASH"     to "💵 Espèces",
        "CARD"     to "💳 Carte",
        "TRANSFER" to "🏦 Virement"
    )

    // Shared field colors
    val fieldColors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor   = indigoPrimary,
        unfocusedBorderColor = indigoPrimary.copy(alpha = 0.25f),
        focusedLabelColor    = indigoPrimary,
        unfocusedLabelColor  = textSecondary,
        cursorColor          = indigoPrimary,
        errorBorderColor     = errorRed,
        errorLabelColor      = errorRed
    )

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState       = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor   = white,
        shape            = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
                .padding(bottom = 40.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {

            // ── Titre + handle ─────────────────────────────────
            Box(
                modifier = Modifier
                    .size(width = 36.dp, height = 4.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(divider)
                    .align(Alignment.CenterHorizontally)
            )

            Spacer(Modifier.height(2.dp))

            // Titre avec badge gradient
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(
                            Brush.linearGradient(colors = listOf(indigoPrimary, blueAccent))
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text     = if (isEditing) "✏️" else "➕",
                        fontSize = 16.sp
                    )
                }
                Text(
                    text       = if (isEditing) "Modifier la dépense" else "Nouvelle dépense",
                    fontSize   = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color      = textPrimary
                )
            }

            HorizontalDivider(color = divider)

            // ── Montant ────────────────────────────────────────
            OutlinedTextField(
                value         = amountText,
                onValueChange = { amountText = it; amountError = false },
                label         = { Text("Montant (MAD)") },
                isError       = amountError,
                supportingText = {
                    if (amountError) Text("Montant invalide ou nul", color = errorRed)
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                singleLine    = true,
                colors        = fieldColors,
                shape         = RoundedCornerShape(12.dp),
                modifier      = Modifier.fillMaxWidth()
            )

            // ── Catégorie ──────────────────────────────────────
            ExposedDropdownMenuBox(
                expanded          = catExpanded,
                onExpandedChange  = { catExpanded = !catExpanded }
            ) {
                OutlinedTextField(
                    value         = selectedCatLabel,
                    onValueChange = {},
                    readOnly      = true,
                    label         = { Text("Catégorie") },
                    isError       = catError,
                    supportingText = {
                        if (catError) Text("Catégorie obligatoire", color = errorRed)
                    },
                    trailingIcon  = {
                        ExposedDropdownMenuDefaults.TrailingIcon(catExpanded)
                    },
                    colors        = fieldColors,
                    shape         = RoundedCornerShape(12.dp),
                    modifier      = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded          = catExpanded,
                    onDismissRequest  = { catExpanded = false },
                    containerColor    = white
                ) {
                    categories.forEach { cat ->
                        DropdownMenuItem(
                            text    = {
                                Text(
                                    "${cat.icon} ${cat.name}",
                                    color = textPrimary
                                )
                            },
                            onClick = {
                                selectedCat = cat.id
                                catExpanded = false
                                catError    = false
                            }
                        )
                    }
                }
            }

            // ── Date ───────────────────────────────────────────
            OutlinedTextField(
                value         = sdf.format(Date(selectedDate)),
                onValueChange = {},
                readOnly      = true,
                label         = { Text("Date") },
                trailingIcon  = {
                    IconButton(onClick = { datePickerDialog.show() }) {
                        Icon(
                            Icons.Default.CalendarToday,
                            contentDescription = "Choisir date",
                            tint               = indigoPrimary
                        )
                    }
                },
                colors  = fieldColors,
                shape   = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            )

            // ── Note ───────────────────────────────────────────
            OutlinedTextField(
                value         = note,
                onValueChange = { note = it },
                label         = { Text("Note (optionnel)") },
                maxLines      = 2,
                colors        = fieldColors,
                shape         = RoundedCornerShape(12.dp),
                modifier      = Modifier.fillMaxWidth()
            )

            // ── Méthode de paiement ────────────────────────────
            ExposedDropdownMenuBox(
                expanded         = payExpanded,
                onExpandedChange = { payExpanded = !payExpanded }
            ) {
                OutlinedTextField(
                    value         = paymentOptions.find { it.first == selectedPayment }?.second
                        ?: "💵 Espèces",
                    onValueChange = {},
                    readOnly      = true,
                    label         = { Text("Méthode de paiement") },
                    trailingIcon  = {
                        ExposedDropdownMenuDefaults.TrailingIcon(payExpanded)
                    },
                    colors  = fieldColors,
                    shape   = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded         = payExpanded,
                    onDismissRequest = { payExpanded = false },
                    containerColor   = white
                ) {
                    paymentOptions.forEach { (key, label) ->
                        DropdownMenuItem(
                            text    = { Text(label, color = textPrimary) },
                            onClick = { selectedPayment = key; payExpanded = false }
                        )
                    }
                }
            }

            // ── Toggle récurrent ───────────────────────────────
            Card(
                shape     = RoundedCornerShape(12.dp),
                colors    = CardDefaults.cardColors(
                    containerColor = if (isRecurring)
                        indigoPrimary.copy(alpha = 0.06f)
                    else
                        surfaceCard
                ),
                elevation = CardDefaults.cardElevation(0.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment     = Alignment.CenterVertically
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment     = Alignment.CenterVertically
                    ) {
                        Text("🔄", fontSize = 20.sp)
                        Column {
                            Text(
                                "Dépense récurrente",
                                fontSize   = 14.sp,
                                fontWeight = FontWeight.Medium,
                                color      = textPrimary
                            )
                            Text(
                                "Se répète chaque mois",
                                fontSize = 12.sp,
                                color    = textSecondary
                            )
                        }
                    }
                    Switch(
                        checked         = isRecurring,
                        onCheckedChange = { isRecurring = it },
                        colors          = SwitchDefaults.colors(
                            checkedThumbColor   = white,
                            checkedTrackColor   = indigoPrimary,
                            uncheckedThumbColor = white,
                            uncheckedTrackColor = textSecondary.copy(alpha = 0.25f)
                        )
                    )
                }
            }

            Spacer(Modifier.height(4.dp))

            // ── Bouton Enregistrer ─────────────────────────────
            Button(
                onClick = {
                    val amount = amountText.replace(",", ".").toDoubleOrNull()
                    var valid  = true
                    if (amount == null || amount <= 0) { amountError = true; valid = false }
                    if (selectedCat <= 0)              { catError    = true; valid = false }
                    if (!valid) return@Button

                    val entity = if (isEditing) {
                        initial!!.copy(
                            amount        = amount!!,
                            categoryId    = selectedCat,
                            date          = selectedDate,
                            note          = note.trim(),
                            updatedAt     = System.currentTimeMillis(),
                            paymentMethod = selectedPayment,
                            isRecurring   = isRecurring
                        )
                    } else {
                        ExpenseEntity(
                            amount        = amount!!,
                            categoryId    = selectedCat,
                            date          = selectedDate,
                            note          = note.trim(),
                            currency      = "MAD",
                            paymentMethod = selectedPayment,
                            isRecurring   = isRecurring
                        )
                    }
                    onSave(entity)
                    onDismiss()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape  = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = indigoPrimary,
                    contentColor   = white
                )
            ) {
                Text(
                    text       = if (isEditing) "Enregistrer les modifications" else "Ajouter la dépense",
                    fontSize   = 15.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            if (isEditing) {
                OutlinedButton(
                    onClick  = onDismiss,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape    = RoundedCornerShape(14.dp),
                    colors   = ButtonDefaults.outlinedButtonColors(contentColor = textSecondary),
                    border   = androidx.compose.foundation.BorderStroke(
                        1.dp, divider
                    )
                ) {
                    Text("Annuler", fontSize = 15.sp)
                }
            }
        }
    }
}