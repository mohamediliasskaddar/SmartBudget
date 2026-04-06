// data/model/MonthlyBudget.kt
package com.example.smartbudget.data.model

data class MonthlyBudget(
    val id: Long,
    val month: String,
    val categoryId: Long,
    val limitAmount: Double
)