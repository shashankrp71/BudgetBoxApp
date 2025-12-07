package com.rick.budgetboxapp.presentation.auth

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rick.budgetboxapp.domain.use_case.LoginUseCase
import com.rick.budgetboxapp.domain.use_case.SignupUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val signupUseCase: SignupUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(AuthState())
    val state = _state.asStateFlow()
    fun login(email: String, password: String) {
        viewModelScope.launch {
            _state.value = AuthState(loading = true)
            val result = loginUseCase.invoke(email, password)
//            val result = loginUseCase(email, password)
            _state.value = if (result.isSuccess)
                AuthState(success = true)
            else{
                Log.d("error",result.exceptionOrNull()?.message.toString())
                AuthState(error = result.exceptionOrNull()?.message)
            }
        }
    }

    fun signup(email: String, password: String) {
        viewModelScope.launch {
            _state.value = AuthState(loading = true)
            val result = signupUseCase.invoke(email, password)
            _state.value = if (result.isSuccess)
                AuthState(success = true)
            else
                AuthState(error = result.exceptionOrNull()?.message)
        }
    }
}
