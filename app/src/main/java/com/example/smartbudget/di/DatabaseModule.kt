// di/DatabaseModule.kt
package com.example.smartbudget.di

import android.content.Context
import com.example.smartbudget.data.local.SmartBudgetDatabase
import com.example.smartbudget.data.local.dao.CategoryDao
import com.example.smartbudget.data.local.dao.ExpenseDao
import com.example.smartbudget.data.local.dao.MonthlyBudgetDao

object DatabaseModule {
    fun provideDatabase(context: Context): SmartBudgetDatabase =
        SmartBudgetDatabase.getInstance(context)

    fun provideExpenseDao(db: SmartBudgetDatabase): ExpenseDao =
        db.expenseDao()

    fun provideCategoryDao(db: SmartBudgetDatabase): CategoryDao =
        db.categoryDao()

    fun provideMonthlyBudgetDao(db: SmartBudgetDatabase): MonthlyBudgetDao =
        db.monthlyBudgetDao()
}