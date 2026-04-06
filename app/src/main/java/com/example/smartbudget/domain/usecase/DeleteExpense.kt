// domain/usecase/DeleteExpense.kt
package com.example.smartbudget.domain.usecase

import com.example.smartbudget.data.local.entity.ExpenseEntity
import com.example.smartbudget.data.repository.ExpenseRepository

class DeleteExpense(private val repo: ExpenseRepository) {
    suspend operator fun invoke(expense: ExpenseEntity) =
        repo.deleteExpense(expense)
}