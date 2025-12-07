package com.rick.budgetboxapp.presentation.auth

data class AuthState(
    val loading: Boolean = false,
    val success: Boolean = false,
    val error: String? = null
)