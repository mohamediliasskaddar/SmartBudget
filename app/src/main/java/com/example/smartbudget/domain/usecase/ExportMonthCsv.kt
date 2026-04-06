// domain/usecase/ExportMonthCsv.kt
package com.example.smartbudget.domain.usecase

import com.example.smartbudget.data.repository.ExpenseRepository
import com.example.smartbudget.util.CsvExporter

class ExportMonthCsv(private val repo: ExpenseRepository) {
    suspend operator fun invoke(year: Int, month: Int): String {
        val expenses = repo.getExpensesForExport(year, month)
        return CsvExporter.buildCsv(expenses)
    }
}