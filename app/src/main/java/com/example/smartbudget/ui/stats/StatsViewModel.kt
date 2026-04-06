// ui/stats/StatsViewModel.kt
package com.example.smartbudget.ui.stats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.smartbudget.data.model.CategoryStats
import com.example.smartbudget.data.repository.ExpenseRepository
import com.example.smartbudget.util.DateUtils
import kotlinx.coroutines.flow.*

data class StatsUiState(
    val year: Int  = DateUtils.currentYear(),
    val month: Int = DateUtils.currentMonth(),
    val total: Double              = 0.0,
    val statsByCategory: List<CategoryStats> = emptyList()
)

class StatsViewModel(private val repo: ExpenseRepository) : ViewModel() {

    private val _year  = MutableStateFlow(DateUtils.currentYear())
    private val _month = MutableStateFlow(DateUtils.currentMonth())

    val uiState: StateFlow<StatsUiState> = combine(_year, _month) { y, m -> y to m }
        .flatMapLatest { (year, month) ->
            combine(
                repo.getTotalForMonth(year, month),
                repo.getStatsByCategory(year, month)
            ) { total, stats ->
                StatsUiState(year = year, month = month, total = total, statsByCategory = stats)
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), StatsUiState())

    fun previousMonth() {
        if (_month.value == 1) { _month.value = 12; _year.value-- }
        else _month.value--
    }

    fun nextMonth() {
        if (_month.value == 12) { _month.value = 1; _year.value++ }
        else _month.value++
    }

    class Factory(private val repo: ExpenseRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return StatsViewModel(repo) as T
        }
    }
}