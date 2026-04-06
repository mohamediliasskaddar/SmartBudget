// ui/expenses/ExpensesViewModel.kt
package com.example.smartbudget.ui.expenses

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.smartbudget.data.local.entity.ExpenseEntity
import com.example.smartbudget.data.repository.CategoryRepository
import com.example.smartbudget.data.repository.ExpenseRepository
import com.example.smartbudget.util.DateUtils
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class ExpensesUiState(
    val year: Int           = DateUtils.currentYear(),
    val month: Int          = DateUtils.currentMonth(),
    val expenses: List<ExpenseEntity>  = emptyList(),
    val total: Double       = 0.0,
    val selectedCategoryId: Long? = null,
    val isLoading: Boolean  = true
)

class ExpensesViewModel(
    private val expenseRepo: ExpenseRepository,
    private val categoryRepo: CategoryRepository
) : ViewModel() {

    private val _year  = MutableStateFlow(DateUtils.currentYear())
    private val _month = MutableStateFlow(DateUtils.currentMonth())
    private val _selectedCategory = MutableStateFlow<Long?>(null)

    val categories = categoryRepo.getActiveCategories()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val uiState: StateFlow<ExpensesUiState> = combine(
        _year, _month, _selectedCategory
    ) { year, month, catId ->
        Triple(year, month, catId)
    }.flatMapLatest { (year, month, catId) ->
        val expFlow = expenseRepo.getExpensesByMonth(year, month)
        val totFlow = expenseRepo.getTotalForMonth(year, month)
        combine(expFlow, totFlow) { expenses, total ->
            val filtered = if (catId != null) expenses.filter { it.categoryId == catId }
            else expenses
            ExpensesUiState(
                year = year, month = month,
                expenses = filtered, total = total,
                selectedCategoryId = catId, isLoading = false
            )
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ExpensesUiState())

    fun previousMonth() {
        if (_month.value == 1) { _month.value = 12; _year.value-- }
        else _month.value--
    }

    fun nextMonth() {
        if (_month.value == 12) { _month.value = 1; _year.value++ }
        else _month.value++
    }

    fun filterByCategory(categoryId: Long?) {
        _selectedCategory.value = categoryId
    }

    fun addExpense(expense: ExpenseEntity) = viewModelScope.launch {
        expenseRepo.addExpense(expense)
    }

    fun updateExpense(expense: ExpenseEntity) = viewModelScope.launch {
        expenseRepo.updateExpense(expense)
    }

    fun deleteExpense(expense: ExpenseEntity) = viewModelScope.launch {
        expenseRepo.deleteExpense(expense)
    }

    class Factory(
        private val expenseRepo: ExpenseRepository,
        private val categoryRepo: CategoryRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return ExpensesViewModel(expenseRepo, categoryRepo) as T
        }
    }
}