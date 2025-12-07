package com.rick.budgetboxapp.domain.use_case

import com.rick.budgetboxapp.domain.repository.AuthRepository
import javax.inject.Inject

class SignupUseCase @Inject constructor(
    private val repo: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String) =
        repo.signup(email, password)
}