package com.rick.budgetboxapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.rick.budgetboxapp.presentation.addexpenses.AddExpenseScreen
import com.rick.budgetboxapp.presentation.auth.AuthScreen
import com.rick.budgetboxapp.presentation.home.HomeScreen
import com.rick.budgetboxapp.presentation.scanexpensees.ScanReceiptScreen


sealed class Screen(val route: String){
    object Auth: Screen("auth")
    object Home: Screen("home")
    object AddExpense: Screen("add_expense")
    object ScanExpense: Screen("scan_expense")
}


@Composable
fun AppNavGraph(
    startDestination: String = Screen.Auth.route
){
    val nav = rememberNavController()
    NavHost(navController = nav, startDestination = startDestination){
        composable(Screen.Auth.route) {
            AuthScreen(
                onLoginSuccess = {
                    nav.navigate(Screen.Home.route) {
                        popUpTo(Screen.Auth.route) { inclusive = true }
                    }
                }
            )
        }
        composable (Screen.Home.route){
            HomeScreen(
                onAddExpense = { nav.navigate(Screen.AddExpense.route)},
                onScan = { nav.navigate(Screen.ScanExpense.route)}
            )
        }
        composable (Screen.AddExpense.route){
            AddExpenseScreen()
        }
        composable (Screen.ScanExpense.route){
            ScanReceiptScreen (
                onExtracted = { nav.navigate(Screen.Home.route)}
            )
        }
    }
}