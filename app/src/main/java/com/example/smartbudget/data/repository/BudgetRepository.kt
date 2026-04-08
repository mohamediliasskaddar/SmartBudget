// data/repository/BudgetRepository.kt
package com.example.smartbudget.data.repository

import com.example.smartbudget.data.local.dao.MonthlyBudgetDao
import com.example.smartbudget.data.local.entity.MonthlyBudgetEntity
import com.example.smartbudget.util.DateUtils
import kotlinx.coroutines.flow.Flow

class BudgetRepository(private val dao: MonthlyBudgetDao) {

    fun getBudgetsForMonth(year: Int, month: Int): Flow<List<MonthlyBudgetEntity>> =
        dao.getBudgetsForMonth(DateUtils.toMonthKey(year, month))

    suspend fun upsertBudget(budget: MonthlyBudgetEntity) = dao.insert(budget)

    suspend fun deleteBudget(budget: MonthlyBudgetEntity) = dao.delete(budget)

    suspend fun getBudgetForCategory(year: Int, month: Int, categoryId: Long): MonthlyBudgetEntity? =
        dao.getBudgetForCategory(DateUtils.toMonthKey(year, month), categoryId)
}