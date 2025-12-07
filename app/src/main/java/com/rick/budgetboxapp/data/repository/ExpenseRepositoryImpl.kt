package com.rick.budgetboxapp.data.repository

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.rick.budgetboxapp.data.local.ExpenseDao
import com.rick.budgetboxapp.data.local.ExpenseEntity
import com.rick.budgetboxapp.data.remote.FirestoreExpense
import com.rick.budgetboxapp.domain.model.Expense
import com.rick.budgetboxapp.domain.repository.ExpenseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import java.io.File
import java.util.UUID
import javax.inject.Inject

class ExpenseRepositoryImpl @Inject constructor(
    private val dao: ExpenseDao,
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth,
    private val context: Context
) : ExpenseRepository {

    override fun getExpenses(): Flow<List<Expense>> =
        dao.getAll()
            .map { list ->
                list.map { it.toDomain() }
            }

    override suspend fun addExpense(expense: Expense) {
        // insert to local DB and sync is false by default
        dao.insert(expense.toEntity(synced = false))
        // try to sync immediately if online and user exists
        val uid = auth.currentUser?.uid
        if (uid != null) {
            try {
                firestore.collection("users").document(uid)
                    .collection("expenses").document(expense.id)
                    .set(expense.toFirestore(uid)).await()
                dao.updateSynced(expense.id, true)
            } catch (e: Exception) {
                // ignore; will be synced later by worker
            }
        }
    }

    override suspend fun syncPendingExpenses(uid: String) {
        val list = dao.getAllOnce().filter { !it.synced }
        list.forEach { entity ->
            val f = FirestoreExpense(
                id = entity.id,
                title = entity.title,
                amount = entity.amount,
                category = entity.category,
                timestamp = entity.timestamp,
                uid = uid
            )
            firestore.collection("users").document(uid)
                .collection("expenses").document(entity.id)
                .set(f).await()
            dao.updateSynced(entity.id, true)
        }
    }

    override suspend fun exportAllExpensesCsv(): String {
        val list = dao.getAllOnce()
        val file = File(context.filesDir, "expenses_export.csv")
        file.writeText("id,title,amount,category,timestamp\n")
        list.forEach {
            file.appendText("${it.id},\"${it.title}\",${it.amount},${it.category},${it.timestamp}\n")
        }
        return file.absolutePath
    }
}

// -- helpers mapping transformers
private fun ExpenseEntity.toDomain() = Expense(id, title, amount, category, timestamp, synced)
private fun Expense.toEntity(synced: Boolean) = ExpenseEntity(id, title, amount, category, timestamp, synced)
private fun Expense.toFirestore(uid: String) = FirestoreExpense(id, title, amount, category, timestamp, uid)
