// util/CurrencyUtils.kt
package com.example.smartbudget.util

object CurrencyUtils {
    fun format(amount: Double, currency: String = "MAD"): String =
        "%.2f %s".format(amount, currency)
}