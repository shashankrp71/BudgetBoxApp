package com.rick.budgetboxapp.domain.model

data class Expense(
    val id: String,
    val title: String,
    val amount: Double,
    val category: String,
    val timestamp: Long,
    val synced: Boolean = false
)