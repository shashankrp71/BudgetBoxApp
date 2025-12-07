

package com.rick.budgetboxapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.google.firebase.auth.FirebaseAuth
import com.rick.budgetboxapp.navigation.AppNavGraph
import com.rick.budgetboxapp.navigation.Screen
import com.rick.budgetboxapp.ui.theme.BudgetBoxAppTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BudgetBoxAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val startDestination = if(firebaseAuth.currentUser != null){
                        Screen.Home.route
                    }else{
                        Screen.Auth.route
                    }
                    AppNavGraph(startDestination)
                }
            }
        }
    }
}

