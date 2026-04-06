// ui/settings/SettingsViewModel.kt
package com.example.smartbudget.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.smartbudget.data.local.entity.CategoryEntity
import com.example.smartbudget.data.repository.CategoryRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsViewModel(private val categoryRepo: CategoryRepository) : ViewModel() {

    val categories = categoryRepo.getAllCategories()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun toggleCategory(category: CategoryEntity) = viewModelScope.launch {
        categoryRepo.setActive(category.id, !category.isActive)
    }

    fun addCategory(name: String, icon: String, color: String) = viewModelScope.launch {
        categoryRepo.addCategory(CategoryEntity(name = name, icon = icon, color = color))
    }

    suspend fun deleteCategory(category: CategoryEntity): Boolean =
        categoryRepo.deleteCategory(category)

    class Factory(private val repo: CategoryRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return SettingsViewModel(repo) as T
        }
    }
}