// domain/usecase/GetCategories.kt
package com.example.smartbudget.domain.usecase

import com.example.smartbudget.data.local.entity.CategoryEntity
import com.example.smartbudget.data.repository.CategoryRepository
import kotlinx.coroutines.flow.Flow

class GetCategories(private val repo: CategoryRepository) {
    operator fun invoke(): Flow<List<CategoryEntity>> =
        repo.getActiveCategories()
}