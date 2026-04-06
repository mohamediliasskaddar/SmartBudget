// di/RepositoryModule.kt
package com.example.smartbudget.di

import com.example.smartbudget.data.repository.BudgetRepository
import com.example.smartbudget.data.repository.CategoryRepository
import com.example.smartbudget.data.repository.ExpenseRepository
import com.example.smartbudget.domain.usecase.*

object RepositoryModule {

    fun provideUseCases(
        expenseRepo: ExpenseRepository,
        categoryRepo: CategoryRepository
    ) = UseCases(
        getExpensesByMonth = GetExpensesByMonth(expenseRepo),
        addExpense         = AddExpense(expenseRepo),
        updateExpense      = UpdateExpense(expenseRepo),
        deleteExpense      = DeleteExpense(expenseRepo),
        getMonthStats      = GetMonthStats(expenseRepo),
        getCategories      = GetCategories(categoryRepo),
        exportMonthCsv     = ExportMonthCsv(expenseRepo)
    )
}

// Conteneur unique de tous les use cases — pratique sans Hilt
data class UseCases(
    val getExpensesByMonth: GetExpensesByMonth,
    val addExpense:         AddExpense,
    val updateExpense:      UpdateExpense,
    val deleteExpense:      DeleteExpense,
    val getMonthStats:      GetMonthStats,
    val getCategories:      GetCategories,
    val exportMonthCsv:     ExportMonthCsv
)