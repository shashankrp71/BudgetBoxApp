package com.rick.budgetboxapp.domain.use_case

import com.rick.budgetboxapp.domain.model.Expense
import com.rick.budgetboxapp.domain.repository.ExpenseRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetExpensesUseCase @Inject constructor(
    private val repo: ExpenseRepository
) {
    operator fun invoke(): Flow<List<Expense>> = repo.getExpenses()
}