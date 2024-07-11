package com.example.expensetracker

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun NavHostScreen(){
        val remember = rememberNavController()
    NavHost(navController = rememberNavController(), startDestination = "home") {
        composable("home") {
            HomeScreen(remember)
        }
        composable("addExpense") {
            AddExpense(remember)
        }
    }
}