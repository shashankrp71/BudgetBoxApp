package com.rick.budgetboxapp.data.remote

data class FirestoreExpense(
    val id: String = "",
    val title: String = "",
    val amount: Double = 0.0,
    val category: String = "",
    val timestamp: Long = 0L,
    val uid: String = ""
)
