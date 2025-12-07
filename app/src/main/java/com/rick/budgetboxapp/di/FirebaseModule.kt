package com.rick.budgetboxapp.di

import android.content.Context
import androidx.room.Room
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.rick.budgetboxapp.common.AppDatabase
import com.rick.budgetboxapp.data.local.ExpenseDao
import com.rick.budgetboxapp.domain.repository.AuthRepository
import com.rick.budgetboxapp.data.repository.AuthRepositoryImpl
import com.rick.budgetboxapp.data.repository.ExpenseRepositoryImpl
import com.rick.budgetboxapp.domain.repository.ExpenseRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideFirestore(): FirebaseFirestore = FirebaseFirestore.getInstance()

    @Provides
    @Singleton
    fun provideStorage(): FirebaseStorage = FirebaseStorage.getInstance()

    @Provides
    @Singleton
    fun provideAuthRepository(auth: FirebaseAuth): AuthRepository =
        AuthRepositoryImpl(auth)

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(appContext, AppDatabase::class.java, "budgetbox_db")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideExpenseDao(db: AppDatabase): ExpenseDao = db.expenseDao()

    @Provides
    @Singleton
    fun provideExpenseRepository(
        dao: ExpenseDao,
        firestore: FirebaseFirestore,
        auth: FirebaseAuth,
        @ApplicationContext context: Context
    ): ExpenseRepository = ExpenseRepositoryImpl(dao, firestore, auth, context)
}