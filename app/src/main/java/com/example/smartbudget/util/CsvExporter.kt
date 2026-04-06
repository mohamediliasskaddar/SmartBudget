// util/CsvExporter.kt
package com.example.smartbudget.util

import com.example.smartbudget.data.local.entity.ExpenseEntity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object CsvExporter {

    private val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    fun buildCsv(expenses: List<ExpenseEntity>): String {
        val header = "id,amount,currency,date,categoryId,note,paymentMethod"
        val rows = expenses.joinToString("\n") { e ->
            "${e.id},${e.amount},${e.currency},${sdf.format(Date(e.date))},${e.categoryId},\"${e.note}\",${e.paymentMethod}"
        }
        return "$header\n$rows"
    }
}