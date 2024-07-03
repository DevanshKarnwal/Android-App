package com.example.whislist

import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import kotlinx.serialization.Serializable

@Composable
fun Navigation(viewModel : WishViewModel = viewModel(), navController: NavHostController = rememberNavController()){
    NavHost(navController = navController
        , startDestination = Screen.HomeScreen.route
        ){
        composable<Home_screen>{
            HomeView(navController,viewModel)
        }
        composable<add_screen>{
            val args = it.toRoute<add_screen>()
            val id = if(args.id ==null){
                0L
            }else{
                args.id
            }
            AddEditDetailView(id = id, viewModel = viewModel, navController)
        }
    }
}

@Serializable
object Home_screen

@Serializable
data class add_screen(val id : Long?){
}