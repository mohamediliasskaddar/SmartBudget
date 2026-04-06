// data/local/dao/MonthlyBudgetDao.kt
package com.example.smartbudget.data.local.dao

import androidx.room.*
import com.example.smartbudget.data.local.entity.MonthlyBudgetEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MonthlyBudgetDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(budget: MonthlyBudgetEntity): Long

    @Update
    suspend fun update(budget: MonthlyBudgetEntity)

    @Delete
    suspend fun delete(budget: MonthlyBudgetEntity)

    @Query("SELECT * FROM monthly_budgets WHERE month = :month")
    fun getBudgetsForMonth(month: String): Flow<List<MonthlyBudgetEntity>>

    @Query("""
        SELECT * FROM monthly_budgets
        WHERE month = :month AND categoryId = :categoryId
    """)
    suspend fun getBudgetForCategory(month: String, categoryId: Long): MonthlyBudgetEntity?
}