package com.rick.budgetboxapp.domain.use_case

import com.rick.budgetboxapp.domain.model.Expense
import com.rick.budgetboxapp.domain.repository.ExpenseRepository
import javax.inject.Inject

class AddExpenseUseCase @Inject constructor(
    private val repo: ExpenseRepository
) {
    suspend operator fun invoke(title: String, amount: Double, category: String) {
        val expense = Expense(
            id = java.util.UUID.randomUUID().toString(),
            title = title,
            amount = amount,
            category = category,
            timestamp = System.currentTimeMillis(),
            synced = false
        )
        repo.addExpense(expense)
    }
}