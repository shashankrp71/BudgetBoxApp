package com.rick.budgetboxapp.presentation.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import com.rick.budgetboxapp.presentation.addexpenses.ExpenseViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onAddExpense: () -> Unit,
    onScan:() -> Unit,
    vm: ExpenseViewModel = hiltViewModel()
) {
    val expenses = vm.expenses.collectAsState().value

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("BudgetBox") })
        },
        floatingActionButton = {
            Column {
                ExtendedFloatingActionButton(
                    text = { Text("Add") },
                    icon = { Icon(Icons.Default.Add, contentDescription = "Add") },
                    onClick = onAddExpense
                )
                Spacer(modifier = Modifier.padding(12.dp))
                ExtendedFloatingActionButton(
                    text = { Text("Scan")},
                    icon = { Icon(Icons.Default.ExitToApp, contentDescription = "Scan")},
                    onClick = onScan
                )
                Spacer(modifier = Modifier.padding(12.dp))
                ExtendedFloatingActionButton(
                    text = { Text("Sync")},
                    icon = { Icon(Icons.Default.Refresh, contentDescription = "Sync")},
                    onClick = { vm.syncPending() }
                )

            }
        }
    ) { padding ->
        if (expenses.isEmpty()) {
            Column(modifier = Modifier
                .fillMaxSize()
                .padding(padding),
                verticalArrangement = Arrangement.Center) {
                Text("No expenses yet. Tap Add to create one.", modifier = Modifier.padding(16.dp))
            }
        } else {
            LazyColumn(modifier = Modifier.padding(12.dp)) {
                items(expenses) { item ->
                    ExpenseRow(item.title, item.amount, item.category, item.timestamp)
                }
            }
        }
    }
}

@Composable
private fun ExpenseRow(title: String, amount: Double, category: String, timestamp: Long) {
    val sdf = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
    val date = sdf.format(Date(timestamp))
    Card(modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp)) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(title, style = MaterialTheme.typography.titleMedium)
                Text("₹${amount}", style = MaterialTheme.typography.titleMedium)
            }
            Spacer(modifier = Modifier.height(4.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(category, style = MaterialTheme.typography.bodySmall)
                Text(date, style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}

