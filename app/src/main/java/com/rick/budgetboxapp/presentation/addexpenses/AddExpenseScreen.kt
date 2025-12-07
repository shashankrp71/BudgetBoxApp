package com.rick.budgetboxapp.presentation.addexpenses

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddExpenseScreen(
    vm: ExpenseViewModel = hiltViewModel(),
    onSaved: (() -> Unit)? = null
) {
    var title by remember { mutableStateOf("") }
    var amountStr by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("Other") }

    val categories = listOf("Food", "Transport", "Shopping", "Bills", "Other")

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("Title") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(value = amountStr, onValueChange = { amountStr = it }, label = { Text("Amount") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(8.dp))
        // Simple category dropdown
        var expanded by remember { mutableStateOf(false) }
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(value = category, onValueChange = {}, label = { Text("Category") }, readOnly = true, modifier = Modifier.fillMaxWidth())
            ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                categories.forEach { c ->
                    DropdownMenuItem(text = { Text(c) }, onClick = {
                        category = c
                        expanded = false
                    })
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            val amount = amountStr.toDoubleOrNull() ?: 0.0
            if (title.isNotBlank() && amount > 0.0) {
                vm.addExpense(title, amount, category)
                onSaved?.invoke()
            }
        }, modifier = Modifier.fillMaxWidth()) {
            Text("Save")
        }
    }
}
