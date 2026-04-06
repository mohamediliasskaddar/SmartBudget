#!/bin/bash

BASE="com/example/smartbudget"

# Create base directories
mkdir -p $BASE

# DATA LAYER
mkdir -p $BASE/data/local/entity
mkdir -p $BASE/data/local/dao
mkdir -p $BASE/data/local
mkdir -p $BASE/data/repository
mkdir -p $BASE/data/model

# DOMAIN LAYER
mkdir -p $BASE/domain/usecase

# UI LAYER
mkdir -p $BASE/ui/expenses
mkdir -p $BASE/ui/stats
mkdir -p $BASE/ui/settings
mkdir -p $BASE/ui/components
mkdir -p $BASE/ui/navigation
mkdir -p $BASE/ui/theme

# DI + UTIL
mkdir -p $BASE/di
mkdir -p $BASE/util

# Root files
#touch $BASE/MainActivity.kt
touch $BASE/SmartBudgetApp.kt
touch $BASE/AppDatabase.kt

# DATA - entity
touch $BASE/data/local/entity/ExpenseEntity.kt
touch $BASE/data/local/entity/CategoryEntity.kt
touch $BASE/data/local/entity/MonthlyBudgetEntity.kt

# DATA - dao
touch $BASE/data/local/dao/ExpenseDao.kt
touch $BASE/data/local/dao/CategoryDao.kt
touch $BASE/data/local/dao/MonthlyBudgetDao.kt

# DATA - local
touch $BASE/data/local/SmartBudgetDatabase.kt

# DATA - repository
touch $BASE/data/repository/ExpenseRepository.kt
touch $BASE/data/repository/CategoryRepository.kt
touch $BASE/data/repository/BudgetRepository.kt

# DATA - model
touch $BASE/data/model/Expense.kt
touch $BASE/data/model/Category.kt
touch $BASE/data/model/MonthlyBudget.kt

# DOMAIN - usecases
touch $BASE/domain/usecase/GetExpensesByMonth.kt
touch $BASE/domain/usecase/AddExpense.kt
touch $BASE/domain/usecase/UpdateExpense.kt
touch $BASE/domain/usecase/DeleteExpense.kt
touch $BASE/domain/usecase/GetMonthStats.kt
touch $BASE/domain/usecase/GetCategories.kt
touch $BASE/domain/usecase/ExportMonthCsv.kt

# UI - expenses
touch $BASE/ui/expenses/ExpensesScreen.kt
touch $BASE/ui/expenses/ExpensesViewModel.kt
touch $BASE/ui/expenses/ExpenseItem.kt
touch $BASE/ui/expenses/AddEditExpenseSheet.kt

# UI - stats
touch $BASE/ui/stats/StatsScreen.kt
touch $BASE/ui/stats/StatsViewModel.kt

# UI - settings
touch $BASE/ui/settings/SettingsScreen.kt
touch $BASE/ui/settings/SettingsViewModel.kt

# UI - components
touch $BASE/ui/components/MonthNavigator.kt
touch $BASE/ui/components/CategoryChip.kt
touch $BASE/ui/components/TotalCard.kt
touch $BASE/ui/components/CategoryPicker.kt
touch $BASE/ui/components/EmptyState.kt

# UI - navigation
touch $BASE/ui/navigation/AppNavigation.kt
touch $BASE/ui/navigation/BottomNavBar.kt

# UI - theme
touch $BASE/ui/theme/Color.kt
touch $BASE/ui/theme/Theme.kt
touch $BASE/ui/theme/Type.kt

# DI
touch $BASE/di/DatabaseModule.kt
touch $BASE/di/RepositoryModule.kt

# UTIL
touch $BASE/util/DateUtils.kt
touch $BASE/util/CsvExporter.kt
touch $BASE/util/CurrencyUtils.kt

echo "✅ Project structure created successfully!"