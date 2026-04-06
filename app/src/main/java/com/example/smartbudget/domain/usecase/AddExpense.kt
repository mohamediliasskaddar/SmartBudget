// domain/usecase/AddExpense.kt
package com.example.smartbudget.domain.usecase

import com.example.smartbudget.data.local.entity.ExpenseEntity
import com.example.smartbudget.data.repository.ExpenseRepository

class AddExpense(private val repo: ExpenseRepository) {
    suspend operator fun invoke(expense: ExpenseEntity): Result<Long> {
        if (expense.amount <= 0) return Result.failure(Exception("Le montant doit être positif"))
        if (expense.categoryId <= 0) return Result.failure(Exception("Catégorie obligatoire"))
        return runCatching { repo.addExpense(expense) }
    }
}