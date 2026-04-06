// data/repository/CategoryRepository.kt
package com.example.smartbudget.data.repository

import com.example.smartbudget.data.local.dao.CategoryDao
import com.example.smartbudget.data.local.entity.CategoryEntity
import kotlinx.coroutines.flow.Flow

class CategoryRepository(private val dao: CategoryDao) {

    fun getActiveCategories(): Flow<List<CategoryEntity>> = dao.getActiveCategories()

    fun getAllCategories(): Flow<List<CategoryEntity>> = dao.getAllCategories()

    suspend fun addCategory(category: CategoryEntity) = dao.insert(category)

    suspend fun updateCategory(category: CategoryEntity) = dao.update(category)

    /**
     * Retourne false si des dépenses existent (suppression interdite).
     */
    suspend fun deleteCategory(category: CategoryEntity): Boolean {
        val count = dao.countExpensesForCategory(category.id)
        return if (count == 0) {
            dao.delete(category)
            true
        } else {
            false
        }
    }

    suspend fun setActive(id: Long, isActive: Boolean) = dao.setActive(id, isActive)
}