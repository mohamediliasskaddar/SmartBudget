// util/CsvImporter.kt
package com.example.smartbudget.util

import com.example.smartbudget.data.local.entity.ExpenseEntity
import java.text.SimpleDateFormat
import java.util.*

object CsvImporter {

    private val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    /**
     * Parse un CSV exporté par SmartBudget.
     * Retourne la liste des entités valides + les lignes en erreur.
     */
    fun parse(csv: String): Pair<List<ExpenseEntity>, List<String>> {
        val lines  = csv.lines().drop(1).filter { it.isNotBlank() }
        val valid  = mutableListOf<ExpenseEntity>()
        val errors = mutableListOf<String>()

        lines.forEach { line ->
            try {
                val cols       = parseCsvLine(line)
                val amount     = cols[1].toDouble()
                val currency   = cols[2].ifBlank { "MAD" }
                val date       = sdf.parse(cols[3])?.time ?: System.currentTimeMillis()
                val categoryId = cols[4].toLong()
                val note       = cols[5].trim('"')
                val payment    = cols.getOrElse(6) { "CASH" }.ifBlank { "CASH" }

                if (amount <= 0 || categoryId <= 0) {
                    errors.add("Ligne ignorée (données invalides) : $line")
                    return@forEach
                }
                valid.add(
                    ExpenseEntity(
                        amount        = amount,
                        currency      = currency,
                        date          = date,
                        categoryId    = categoryId,
                        note          = note,
                        paymentMethod = payment
                    )
                )
            } catch (e: Exception) {
                errors.add("Erreur parsing : $line")
            }
        }
        return Pair(valid, errors)
    }

    // Gère les champs entre guillemets contenant des virgules
    private fun parseCsvLine(line: String): List<String> {
        val result  = mutableListOf<String>()
        val current = StringBuilder()
        var inQuote = false
        for (ch in line) {
            when {
                ch == '"'           -> inQuote = !inQuote
                ch == ',' && !inQuote -> { result.add(current.toString()); current.clear() }
                else                -> current.append(ch)
            }
        }
        result.add(current.toString())
        return result
    }
}