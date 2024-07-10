package com.example.firebaseproject1.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.firebaseproject1.components.HeadingTextComponent
import com.example.firebaseproject1.navigation.PostOfficeAppRouter
import com.example.firebaseproject1.navigation.Screen
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

@Composable
fun TermsAndConditions() {
    Surface(
        Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)) {
        HeadingTextComponent(value = "Terms Of Use")

    }
    runBlocking {

        PostOfficeAppRouter.navigateTo(Screen.SignUpScreen)
//        delay(5000)
    }
}