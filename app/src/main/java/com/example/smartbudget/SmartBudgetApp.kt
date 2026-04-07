// SmartBudgetApp.kt
package com.example.smartbudget

import android.app.Application
import com.example.smartbudget.data.local.SmartBudgetDatabase
import com.example.smartbudget.data.repository.BudgetRepository
import com.example.smartbudget.data.repository.CategoryRepository
import com.example.smartbudget.data.repository.ExpenseRepository
import com.example.smartbudget.di.RepositoryModule
import com.example.smartbudget.di.UseCases

class SmartBudgetApp : Application() {

    val database by lazy { SmartBudgetDatabase.getInstance(this) }

    val expenseRepository  by lazy { ExpenseRepository(database.expenseDao()) }
    val categoryRepository by lazy { CategoryRepository(database.categoryDao()) }
    val budgetRepository   by lazy { BudgetRepository(database.monthlyBudgetDao()) }

    val useCases: UseCases by lazy {
        RepositoryModule.provideUseCases(expenseRepository, categoryRepository)
    }
}