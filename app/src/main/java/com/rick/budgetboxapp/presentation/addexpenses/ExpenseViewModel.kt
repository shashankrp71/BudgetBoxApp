package com.rick.budgetboxapp.presentation.addexpenses


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rick.budgetboxapp.domain.use_case.AddExpenseUseCase
import com.rick.budgetboxapp.domain.use_case.GetExpensesUseCase
import com.rick.budgetboxapp.domain.use_case.SyncExpensesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExpenseViewModel @Inject constructor(
    private val addExpenseUseCase: AddExpenseUseCase,
    getExpensesUseCase: GetExpensesUseCase,
    private val syncExpensesUseCase: SyncExpensesUseCase
) : ViewModel() {

    val expenses: StateFlow<List<com.rick.budgetboxapp.domain.model.Expense>> =
        getExpensesUseCase.invoke().stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    fun addExpense(title: String, amount: Double, category: String) {
        viewModelScope.launch {
            addExpenseUseCase.invoke(title, amount, category)
        }
    }

    fun syncPending() {
        viewModelScope.launch {
            syncExpensesUseCase.invoke()
            // can call directly invoke is not necessary
        }
    }
}
