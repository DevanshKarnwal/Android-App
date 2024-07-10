package com.example.firebaseproject1.app

import com.example.firebaseproject1.data.LogInViewModel
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.firebaseproject1.navigation.PostOfficeAppRouter
import com.example.firebaseproject1.navigation.Screen
import com.example.firebaseproject1.screens.HomeScreen
import com.example.firebaseproject1.screens.LoginScreen
import com.example.firebaseproject1.screens.SignUpScreen
import com.example.firebaseproject1.screens.TermsAndConditions

@Composable
fun PostOfficeApp(){
    Surface(modifier = Modifier.fillMaxSize(),
        color = Color.White) {
        Crossfade(targetState = PostOfficeAppRouter.currentScreen) {
            currentState ->
            when(currentState.value){
                is Screen.SignUpScreen ->{
                    SignUpScreen()
                }
                is Screen.TermsAndConditionScreen ->{
                    TermsAndConditions()
                }
                is Screen.LoginScreen ->{
                    LoginScreen()
                }
                is Screen.HomeScreen ->{
                    HomeScreen()
                }
            }
        }
    }
}