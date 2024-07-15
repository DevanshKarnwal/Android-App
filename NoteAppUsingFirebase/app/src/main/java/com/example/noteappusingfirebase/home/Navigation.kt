package com.example.noteappusingfirebase.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.internal.composableLambda
import androidx.compose.ui.Modifier
import androidx.navigation.NavArgument
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.noteappusingfirebase.detail.DetailScreen
import com.example.noteappusingfirebase.detail.DetailViewModel
import com.example.noteappusingfirebase.login.LoginScreen
import com.example.noteappusingfirebase.login.LoginViewModel
import com.example.noteappusingfirebase.login.SignUpScreen
import kotlin.math.log


enum class LogInRoutes{
    SignUp,
    SignIn
}

enum class HomeRoutes{
    Home,
    Detail
}

enum class NestedRoute{
    Main,
    Login
}

@Composable
fun Navigation(
    navController: NavHostController = rememberNavController(),
    loginViewModel: LoginViewModel,
    detailViewModel: DetailViewModel,
    homeViewModel: HomeViewModel
) {

    NavHost(navController = navController, startDestination = NestedRoute.Main.name ) {

        authGraph(navController, loginViewModel)
        homeGraph(navController,detailViewModel,homeViewModel)

    }

}

fun NavGraphBuilder.authGraph(navController: NavHostController, loginViewModel: LoginViewModel){
        navigation(startDestination = LogInRoutes.SignIn.name,route = NestedRoute.Login.name){
            composable(route = LogInRoutes.SignIn.name){
                LoginScreen(onNavToHomepage = {
                    navController.navigate(NestedRoute.Main.name){
                        launchSingleTop = true
                        popUpTo(route = LogInRoutes.SignIn.name){
                            inclusive = true
                        }
                    }
                },
                    loginViewModel = loginViewModel
                ) {
                    navController.navigate(LogInRoutes.SignUp.name){
                        launchSingleTop = true
                        popUpTo(route = LogInRoutes.SignIn.name){
                            inclusive = true
                        }
                    }
                }
            }

            composable(LogInRoutes.SignUp.name){
                SignUpScreen(onNavToHomepage = {
                    navController.navigate(NestedRoute.Main.name){
                        popUpTo(route = LogInRoutes.SignUp.name){
                            inclusive = true
                        }
                    }
                },
                    loginViewModel = loginViewModel
                ){
                    navController.navigate(LogInRoutes.SignIn.name)
                }
            }
        }
}

fun NavGraphBuilder.homeGraph(
    navController: NavHostController,
    detailViewModel: DetailViewModel,
    homeViewModel: HomeViewModel
){
    navigation(startDestination = HomeRoutes.Home.name,route = NestedRoute.Main.name){
        composable(HomeRoutes.Home.name){
            Home(homeViewModel = homeViewModel,
                onNoteClicked = {
                    noteId -> navController.navigate(HomeRoutes.Detail.name +"?id = $noteId"){
                        launchSingleTop = true
                    }
                },
                navToDetailPage = {
                    navController.navigate(HomeRoutes.Detail.name)
                }
                ){
                navController.navigate(NestedRoute.Login.name){
                    launchSingleTop = true
                    popUpTo(0){
                        inclusive = true
                    }
                }

            }
        }
        composable(route = HomeRoutes.Detail.name + "?id={id}",
                arguments = listOf(navArgument("id"){
                    type = NavType.StringType
                    defaultValue = ""
                })
            ){
            entry ->
                    DetailScreen(detailViewModel = detailViewModel,
                        noteId = entry.arguments?.getString("id") as String,) {
                        navController.navigateUp()
                    }
        }
    }
}