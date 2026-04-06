package com.example.smartbudget.data.model;


data class CategoryStats(
    val categoryId: Long,
    val categoryName: String,
    val categoryIcon: String,
    val categoryColor: String,
    val total: Double,
    val count: Int
)