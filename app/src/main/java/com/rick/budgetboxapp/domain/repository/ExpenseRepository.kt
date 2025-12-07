package com.rick.budgetboxapp.domain.repository

import com.rick.budgetboxapp.domain.model.Expense
import kotlinx.coroutines.flow.Flow

interface ExpenseRepository {
    fun getExpenses(): Flow<List<Expense>>
    suspend fun addExpense(expense: Expense)
    suspend fun syncPendingExpenses(uid: String)
    suspend fun exportAllExpensesCsv(): String
}