// ui/settings/SettingsViewModel.kt
package com.example.smartbudget.ui.settings

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.smartbudget.data.local.entity.CategoryEntity
import com.example.smartbudget.data.repository.CategoryRepository
import com.example.smartbudget.data.repository.ExpenseRepository
import com.example.smartbudget.util.CsvExporter
import com.example.smartbudget.util.CsvImporter
import com.example.smartbudget.util.DateUtils
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.io.File

data class SettingsUiState(
    val exportMessage: String? = null,
    val importMessage: String? = null
)

class SettingsViewModel(
    private val categoryRepo: CategoryRepository,
    private val expenseRepo: ExpenseRepository
) : ViewModel() {

    val categories = categoryRepo.getAllCategories()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    fun toggleCategory(category: CategoryEntity) = viewModelScope.launch {
        categoryRepo.setActive(category.id, !category.isActive)
    }

    fun addCategory(name: String, icon: String, color: String) = viewModelScope.launch {
        categoryRepo.addCategory(CategoryEntity(name = name, icon = icon, color = color))
    }

    suspend fun deleteCategory(category: CategoryEntity): Boolean =
        categoryRepo.deleteCategory(category)

    // ── Export CSV ────────────────────────────────────────────────
    fun exportCurrentMonth(context: Context) = viewModelScope.launch {
        val year  = DateUtils.currentYear()
        val month = DateUtils.currentMonth()

        val csv  = buildString {
            val expenses = expenseRepo.getExpensesForExport(year, month)
            append(CsvExporter.buildCsv(expenses))
        }

        if (csv.lines().size <= 1) {
            _uiState.update { it.copy(exportMessage = "Aucune dépense à exporter ce mois-ci.") }
            return@launch
        }

        try {
            val fileName = "smartbudget_${DateUtils.toMonthKey(year, month)}.csv"
            val file     = File(context.cacheDir, fileName)
            file.writeText(csv)

            val uri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.provider",
                file
            )
            val intent = Intent(Intent.ACTION_SEND).apply {
                type     = "text/csv"
                putExtra(Intent.EXTRA_STREAM, uri)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            context.startActivity(Intent.createChooser(intent, "Exporter CSV"))
            _uiState.update { it.copy(exportMessage = "Export réussi : $fileName") }
        } catch (e: Exception) {
            _uiState.update { it.copy(exportMessage = "Erreur export : ${e.message}") }
        }
    }

    // ── Import CSV ────────────────────────────────────────────────
    fun importFromUri(context: Context, uri: Uri) = viewModelScope.launch {
        try {
            val csv = context.contentResolver
                .openInputStream(uri)
                ?.bufferedReader()
                ?.readText()
                ?: run {
                    _uiState.update { it.copy(importMessage = "Impossible de lire le fichier.") }
                    return@launch
                }

            val (expenses, errors) = CsvImporter.parse(csv)
            expenses.forEach { expenseRepo.addExpense(it) }

            val msg = "${expenses.size} dépense(s) importée(s)." +
                    if (errors.isNotEmpty()) " ${errors.size} ligne(s) ignorée(s)." else ""
            _uiState.update { it.copy(importMessage = msg) }
        } catch (e: Exception) {
            _uiState.update { it.copy(importMessage = "Erreur import : ${e.message}") }
        }
    }

    fun clearMessages() = _uiState.update {
        it.copy(exportMessage = null, importMessage = null)
    }

    class Factory(
        private val categoryRepo: CategoryRepository,
        private val expenseRepo: ExpenseRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return SettingsViewModel(categoryRepo, expenseRepo) as T
        }
    }
}