package com.rick.budgetboxapp.common


import androidx.room.Database
import androidx.room.RoomDatabase
import com.rick.budgetboxapp.data.local.ExpenseDao
import com.rick.budgetboxapp.data.local.ExpenseEntity

@Database(entities = [ExpenseEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun expenseDao(): ExpenseDao
}
