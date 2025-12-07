package com.rick.budgetboxapp.domain.use_case

import com.google.firebase.auth.FirebaseAuth
import com.rick.budgetboxapp.domain.repository.ExpenseRepository
import javax.inject.Inject

class SyncExpensesUseCase @Inject constructor(
    private val repo: ExpenseRepository,
    private val auth: FirebaseAuth
) {
    suspend operator fun invoke() {
        val uid = auth.currentUser?.uid ?: return
        repo.syncPendingExpenses(uid)
    }
}