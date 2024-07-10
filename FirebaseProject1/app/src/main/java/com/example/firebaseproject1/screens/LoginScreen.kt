package com.example.firebaseproject1.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.firebaseproject1.R
import com.example.firebaseproject1.components.ButtonComponent
import com.example.firebaseproject1.components.ClickableLoginTextComponent
import com.example.firebaseproject1.components.DividerTextComponent
import com.example.firebaseproject1.components.HeadingTextComponent
import com.example.firebaseproject1.components.MyTextField
import com.example.firebaseproject1.components.NormalTextComponent
import com.example.firebaseproject1.components.PasswordTextField
import com.example.firebaseproject1.components.UnderlineTextComponent
import com.example.firebaseproject1.data.LogInUiEvent
import com.example.firebaseproject1.data.LogInViewModel
import com.example.firebaseproject1.navigation.PostOfficeAppRouter
import com.example.firebaseproject1.navigation.Screen

@Composable
fun LoginScreen(logInViewModel: LogInViewModel = viewModel()) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {

        Surface(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(28.dp)
        ){
            Column(modifier = Modifier.fillMaxSize()) {
                NormalTextComponent(value = stringResource(id = R.string.hello))
                HeadingTextComponent(value = "Welcome back")
                Spacer(modifier = Modifier.height(20.dp))
                MyTextField(label = "Email", imageVector = Icons.Default.Email,onTextSelected = {
                    logInViewModel.onEvent(LogInUiEvent.EmailChanged(it))
                },
                    errorStatus = logInViewModel.loginUiState.value.emailError
                    )
                PasswordTextField(label = "Password", imageVector = Icons.Default.Lock, onTextSelected = {
                    logInViewModel.onEvent(LogInUiEvent.PasswordChanged(it))
                },
                    errorStatus = logInViewModel.loginUiState.value.passwordError

                )
                Spacer(modifier = Modifier.height(40.dp))
                UnderlineTextComponent(value = "Forgot your password")
                Spacer(modifier = Modifier.height(40.dp))
                ButtonComponent(value = "Login", onButtonClicked = {
                        logInViewModel.onEvent(LogInUiEvent.LogInButtonClicked)
                },
                    isEnabled = logInViewModel.allValidatonPassed.value
                )
                Spacer(modifier = Modifier.height(40.dp))
                DividerTextComponent()
                ClickableLoginTextComponent (false){
                    PostOfficeAppRouter.navigateTo(Screen.SignUpScreen)

                }
            }

        }
        if(logInViewModel.logInInProgress.value){
        CircularProgressIndicator()
        }
    }
    BackHandler (true){
        PostOfficeAppRouter.navigateTo(Screen.SignUpScreen)
    }
}