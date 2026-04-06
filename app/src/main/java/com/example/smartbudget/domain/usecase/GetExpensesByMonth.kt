// domain/usecase/GetExpensesByMonth.kt
package com.example.smartbudget.domain.usecase

import com.example.smartbudget.data.local.entity.ExpenseEntity
import com.example.smartbudget.data.repository.ExpenseRepository
import kotlinx.coroutines.flow.Flow

class GetExpensesByMonth(private val repo: ExpenseRepository) {
    operator fun invoke(year: Int, month: Int): Flow<List<ExpenseEntity>> =
        repo.getExpensesByMonth(year, month)
}