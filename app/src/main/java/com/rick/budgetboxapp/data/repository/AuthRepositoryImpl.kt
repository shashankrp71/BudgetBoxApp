package com.rick.budgetboxapp.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.rick.budgetboxapp.domain.repository.AuthRepository
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth
): AuthRepository {

    override suspend fun login(email: String, password: String): Result<Unit> =
        try {
            firebaseAuth.signInWithEmailLink(email,password).await()
            Result.success(Unit)
        }catch (e: Exception){
            e.printStackTrace()
            Result.failure(e)
        }


    override suspend fun signup(email: String, password: String): Result<Unit> =
        try {
            firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }

    override fun logout() {
        firebaseAuth.signOut()
    }

    override fun currentUser(): FirebaseUser? = firebaseAuth.currentUser

}