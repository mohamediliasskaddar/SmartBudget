// data/repository/ExpenseRepository.kt
package com.example.smartbudget.data.repository

import com.example.smartbudget.data.local.dao.ExpenseDao
import com.example.smartbudget.data.local.entity.ExpenseEntity
import com.example.smartbudget.data.model.CategoryStats
import com.example.smartbudget.data.model.Expense
import com.example.smartbudget.util.DateUtils
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ExpenseRepository(private val dao: ExpenseDao) {

    fun getExpensesByMonth(year: Int, month: Int): Flow<List<ExpenseEntity>> {
        val (start, end) = DateUtils.monthBounds(year, month)
        return dao.getExpensesByMonth(start, end)
    }

    fun getTotalForMonth(year: Int, month: Int): Flow<Double> {
        val (start, end) = DateUtils.monthBounds(year, month)
        return dao.getTotalForMonth(start, end)
    }

    fun getStatsByCategory(year: Int, month: Int): Flow<List<CategoryStats>> {
        val (start, end) = DateUtils.monthBounds(year, month)
        return dao.getStatsByCategory(start, end)
    }

    suspend fun addExpense(expense: ExpenseEntity): Long = dao.insert(expense)

    suspend fun updateExpense(expense: ExpenseEntity) = dao.update(expense)

    suspend fun deleteExpense(expense: ExpenseEntity) = dao.delete(expense)

    suspend fun getExpensesForExport(year: Int, month: Int): List<ExpenseEntity> {
        val (start, end) = DateUtils.monthBounds(year, month)
        return dao.getExpensesForExport(start, end)
    }
}