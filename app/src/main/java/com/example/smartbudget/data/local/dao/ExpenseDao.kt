// data/local/dao/ExpenseDao.kt
package com.example.smartbudget.data.local.dao

import androidx.room.*
import com.example.smartbudget.data.local.entity.ExpenseEntity
import com.example.smartbudget.data.model.CategoryStats
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(expense: ExpenseEntity): Long

    @Update
    suspend fun update(expense: ExpenseEntity)

    @Delete
    suspend fun delete(expense: ExpenseEntity)

    @Query("SELECT * FROM expenses WHERE id = :id")
    suspend fun getExpenseById(id: Long): ExpenseEntity?

    // Dépenses d'un mois donné (startMs..endMs = bornes epoch du mois)
    @Query("""
        SELECT * FROM expenses
        WHERE date >= :startMs AND date < :endMs
        ORDER BY date DESC
    """)
    fun getExpensesByMonth(startMs: Long, endMs: Long): Flow<List<ExpenseEntity>>

    // Filtre par catégorie dans un mois
    @Query("""
        SELECT * FROM expenses
        WHERE date >= :startMs AND date < :endMs
          AND categoryId = :categoryId
        ORDER BY date DESC
    """)
    fun getExpensesByMonthAndCategory(
        startMs: Long,
        endMs: Long,
        categoryId: Long
    ): Flow<List<ExpenseEntity>>

    // Total du mois
    @Query("""
        SELECT COALESCE(SUM(amount), 0.0)
        FROM expenses
        WHERE date >= :startMs AND date < :endMs
    """)
    fun getTotalForMonth(startMs: Long, endMs: Long): Flow<Double>

    // Répartition par catégorie (JOIN avec categories)
    @Query("""
        SELECT
            e.categoryId,
            c.name   AS categoryName,
            c.icon   AS categoryIcon,
            c.color  AS categoryColor,
            SUM(e.amount)  AS total,
            COUNT(e.id)    AS count
        FROM expenses e
        INNER JOIN categories c ON e.categoryId = c.id
        WHERE e.date >= :startMs AND e.date < :endMs
        GROUP BY e.categoryId
        ORDER BY total DESC
    """)
    fun getStatsByCategory(startMs: Long, endMs: Long): Flow<List<CategoryStats>>

    // Export CSV : toutes les dépenses d'un mois (snapshot)
    @Query("""
        SELECT * FROM expenses
        WHERE date >= :startMs AND date < :endMs
        ORDER BY date ASC
    """)
    suspend fun getExpensesForExport(startMs: Long, endMs: Long): List<ExpenseEntity>
}