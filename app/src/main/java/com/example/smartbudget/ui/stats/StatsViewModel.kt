// ui/stats/StatsViewModel.kt
package com.example.smartbudget.ui.stats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.smartbudget.data.local.entity.MonthlyBudgetEntity
import com.example.smartbudget.data.model.CategoryStats
import com.example.smartbudget.data.repository.BudgetRepository
import com.example.smartbudget.data.repository.CategoryRepository
import com.example.smartbudget.data.repository.ExpenseRepository
import com.example.smartbudget.util.DateUtils
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class CategoryBudgetRow(
    val stats:       CategoryStats,
    val budgetLimit: Double?,           // null = pas de budget défini
    val progress:    Float              // 0f..1f+  (peut dépasser 1 si over budget)
)

data class StatsUiState(
    val year: Int  = DateUtils.currentYear(),
    val month: Int = DateUtils.currentMonth(),
    val total:              Double                  = 0.0,
    val statsByCategory:    List<CategoryStats>     = emptyList(),
    val budgetRows:         List<CategoryBudgetRow> = emptyList(),
    val budgets:            List<MonthlyBudgetEntity> = emptyList(),
    val topCategory:        CategoryStats?          = null
)

class StatsViewModel(
    private val expenseRepo:  ExpenseRepository,
    private val budgetRepo:   BudgetRepository,
    private val categoryRepo: CategoryRepository
) : ViewModel() {

    private val _year  = MutableStateFlow(DateUtils.currentYear())
    private val _month = MutableStateFlow(DateUtils.currentMonth())

    val uiState: StateFlow<StatsUiState> = combine(_year, _month) { y, m -> y to m }
        .flatMapLatest { (year, month) ->
            combine(
                expenseRepo.getTotalForMonth(year, month),
                expenseRepo.getStatsByCategory(year, month),
                budgetRepo.getBudgetsForMonth(year, month)
            ) { total, stats, budgets ->
                val budgetMap = budgets.associateBy { it.categoryId }
                val rows = stats.map { s ->
                    val limit = budgetMap[s.categoryId]?.limitAmount
                    CategoryBudgetRow(
                        stats       = s,
                        budgetLimit = limit,
                        progress    = if (limit != null && limit > 0)
                            (s.total / limit).toFloat()
                        else 0f
                    )
                }
                StatsUiState(
                    year             = year,
                    month            = month,
                    total            = total,
                    statsByCategory  = stats,
                    budgetRows       = rows,
                    budgets          = budgets,
                    topCategory      = stats.firstOrNull()
                )
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), StatsUiState())

    fun previousMonth() {
        if (_month.value == 1) { _month.value = 12; _year.value-- } else _month.value--
    }

    fun nextMonth() {
        if (_month.value == 12) { _month.value = 1; _year.value++ } else _month.value++
    }

    fun upsertBudget(categoryId: Long, amount: Double) = viewModelScope.launch {
        budgetRepo.upsertBudget(
            MonthlyBudgetEntity(
                month       = DateUtils.toMonthKey(_year.value, _month.value),
                categoryId  = categoryId,
                limitAmount = amount
            )
        )
    }

    fun deleteBudget(budget: MonthlyBudgetEntity) = viewModelScope.launch {
        budgetRepo.deleteBudget(budget)
    }

    class Factory(
        private val expenseRepo:  ExpenseRepository,
        private val budgetRepo:   BudgetRepository,
        private val categoryRepo: CategoryRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return StatsViewModel(expenseRepo, budgetRepo, categoryRepo) as T
        }
    }
}