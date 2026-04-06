// data/local/SmartBudgetDatabase.kt
package com.example.smartbudget.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.smartbudget.data.local.dao.CategoryDao
import com.example.smartbudget.data.local.dao.ExpenseDao
import com.example.smartbudget.data.local.dao.MonthlyBudgetDao
import com.example.smartbudget.data.local.entity.CategoryEntity
import com.example.smartbudget.data.local.entity.ExpenseEntity
import com.example.smartbudget.data.local.entity.MonthlyBudgetEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        CategoryEntity::class,
        ExpenseEntity::class,
        MonthlyBudgetEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class SmartBudgetDatabase : RoomDatabase() {

    abstract fun categoryDao(): CategoryDao
    abstract fun expenseDao(): ExpenseDao
    abstract fun monthlyBudgetDao(): MonthlyBudgetDao

    companion object {
        @Volatile private var INSTANCE: SmartBudgetDatabase? = null

        fun getInstance(context: Context): SmartBudgetDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    SmartBudgetDatabase::class.java,
                    "smartbudget.db"
                )
                    .addCallback(object : Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            // Pré-remplir les catégories par défaut
                            INSTANCE?.let { database ->
                                CoroutineScope(Dispatchers.IO).launch {
                                    database.categoryDao().insert(CategoryEntity(name="Alimentation", icon="🍔", color="#FF5722"))
                                    database.categoryDao().insert(CategoryEntity(name="Transport",    icon="🚌", color="#2196F3"))
                                    database.categoryDao().insert(CategoryEntity(name="Logement",     icon="🏠", color="#9C27B0"))
                                    database.categoryDao().insert(CategoryEntity(name="Santé",        icon="💊", color="#4CAF50"))
                                    database.categoryDao().insert(CategoryEntity(name="Loisirs",      icon="🎮", color="#FF9800"))
                                    database.categoryDao().insert(CategoryEntity(name="Études",       icon="📚", color="#00BCD4"))
                                    database.categoryDao().insert(CategoryEntity(name="Autre",        icon="📦", color="#607D8B"))
                                }
                            }
                        }
                    })
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}