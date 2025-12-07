package com.rick.budgetboxapp.common


import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.rick.budgetboxapp.domain.repository.ExpenseRepository

import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class ExportWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val repo: ExpenseRepository
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        return try {
            val path = repo.exportAllExpensesCsv()
            // you could upload the file or notify the user here
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }
}
