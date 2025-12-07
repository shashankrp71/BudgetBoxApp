package com.rick.budgetboxapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.rick.budgetboxapp.presentation.addexpenses.AddExpenseScreen
import com.rick.budgetboxapp.presentation.auth.AuthScreen
import com.rick.budgetboxapp.presentation.home.HomeScreen


sealed class Screen(val route: String){
    object Auth: Screen("auth")
    object Home: Screen("home")
    object AddExpense: Screen("add_expense")
}


@Composable
fun AppNavGraph(){
    val nav = rememberNavController()
    NavHost(navController = nav, startDestination = Screen.Auth.route){
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
            HomeScreen(onAddExpense = { nav.navigate(Screen.AddExpense.route)})
        }
        composable (Screen.AddExpense.route){
            AddExpenseScreen()
        }
    }
}