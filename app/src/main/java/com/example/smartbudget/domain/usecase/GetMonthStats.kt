// domain/usecase/GetMonthStats.kt
package com.example.smartbudget.domain.usecase

import com.example.smartbudget.data.model.CategoryStats
import com.example.smartbudget.data.repository.ExpenseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

data class MonthStats(
    val total: Double,
    val byCategory: List<CategoryStats>,
    val topCategory: CategoryStats?
)

class GetMonthStats(private val repo: ExpenseRepository) {
    operator fun invoke(year: Int, month: Int): Flow<MonthStats> =
        combine(
            repo.getTotalForMonth(year, month),
            repo.getStatsByCategory(year, month)
        ) { total, stats ->
            MonthStats(
                total       = total,
                byCategory  = stats,
                topCategory = stats.firstOrNull()
            )
        }
}