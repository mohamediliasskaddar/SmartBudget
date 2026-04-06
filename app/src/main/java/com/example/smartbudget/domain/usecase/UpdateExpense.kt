// domain/usecase/UpdateExpense.kt
package com.example.smartbudget.domain.usecase

import com.example.smartbudget.data.local.entity.ExpenseEntity
import com.example.smartbudget.data.repository.ExpenseRepository

class UpdateExpense(private val repo: ExpenseRepository) {
    suspend operator fun invoke(expense: ExpenseEntity): Result<Unit> {
        if (expense.amount <= 0) return Result.failure(Exception("Le montant doit être positif"))
        if (expense.categoryId <= 0) return Result.failure(Exception("Catégorie obligatoire"))
        return runCatching { repo.updateExpense(expense) }
    }
}