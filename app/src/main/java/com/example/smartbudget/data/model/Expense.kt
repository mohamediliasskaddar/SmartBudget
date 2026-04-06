// data/model/Expense.kt
package com.example.smartbudget.data.model

data class Expense(
    val id: Long,
    val amount: Double,
    val currency: String,
    val date: Long,
    val categoryId: Long,
    val categoryName: String,
    val categoryIcon: String,
    val categoryColor: String,
    val note: String,
    val paymentMethod: String,
    val createdAt: Long,
    val updatedAt: Long
)