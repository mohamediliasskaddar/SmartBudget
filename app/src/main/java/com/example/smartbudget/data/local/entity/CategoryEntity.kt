// data/local/entity/CategoryEntity.kt
package com.example.smartbudget.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "categories",
    indices = [Index(value = ["name"], unique = true)]
)
data class CategoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val icon: String,       // emoji ou nom d'icône, ex: "🍔"
    val color: String,      // hex string, ex: "#FF5722"
    val isActive: Boolean = true
)