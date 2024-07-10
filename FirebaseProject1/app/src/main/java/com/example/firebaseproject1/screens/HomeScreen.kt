package com.example.firebaseproject1.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.firebaseproject1.components.ButtonComponent
import com.example.firebaseproject1.components.HeadingTextComponent
import com.example.firebaseproject1.data.LogInUiEvent
import com.example.firebaseproject1.data.LogInViewModel

@Composable
fun HomeScreen() {
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(28.dp)
    ){
        val loginViewModel =LogInViewModel()
        Column(modifier = Modifier.fillMaxWidth()) {
            HeadingTextComponent(value = "Home")

            ButtonComponent(value = "Logout", onButtonClicked = { loginViewModel.logOut() })
        }
    }
}