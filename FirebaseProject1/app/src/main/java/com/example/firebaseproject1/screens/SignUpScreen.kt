package com.example.firebaseproject1.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
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
import com.example.firebaseproject1.components.CheckBoxComponent
import com.example.firebaseproject1.components.ClickableLoginTextComponent
import com.example.firebaseproject1.components.DividerTextComponent
import com.example.firebaseproject1.components.HeadingTextComponent
import com.example.firebaseproject1.components.MyTextField
import com.example.firebaseproject1.components.NormalTextComponent
import com.example.firebaseproject1.components.PasswordTextField
import com.example.firebaseproject1.data.SignUpViewModel
import com.example.firebaseproject1.data.UIEvent
import com.example.firebaseproject1.navigation.PostOfficeAppRouter
import com.example.firebaseproject1.navigation.Screen

@Composable
fun SignUpScreen(signUpViewModel: SignUpViewModel = viewModel()) {
    Box (modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(28.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
            ) {
                NormalTextComponent(value = stringResource(id = R.string.hello))
                HeadingTextComponent(value = stringResource(id = R.string.createAccont))
                Spacer(modifier = Modifier.height(16.dp))
                MyTextField(label = "First Name", imageVector = Icons.Default.Person, onTextSelected = {
                    signUpViewModel.onEvent(UIEvent.FirstNameChanged(it) )
                } , signUpViewModel.registrationUIState.value.firstNameError)
                MyTextField(label = "Last Name", imageVector = Icons.Default.Person, onTextSelected = {
                    signUpViewModel.onEvent(UIEvent.LastNameChanged(it))
                }, signUpViewModel.registrationUIState.value.lastNameError)
                MyTextField(label = "Email ",imageVector = Icons.Default.Email, onTextSelected = {
                    signUpViewModel.onEvent(UIEvent.EmailChanged(it))
                }, signUpViewModel.registrationUIState.value.emailError)
                PasswordTextField(label = "Password", imageVector = Icons.Default.Lock, onTextSelected = {
                    signUpViewModel.onEvent(UIEvent.PasswordChanged(it))
                }, signUpViewModel.registrationUIState.value.passwordError)
                CheckBoxComponent(
                    value = "By continuing you accept our terms and conditions",
                    onTextSelected = {
                        PostOfficeAppRouter.navigateTo(Screen.TermsAndConditionScreen)
                    }, onCheckedChange = {
                        signUpViewModel.onEvent(UIEvent.PrivacyPolicyCheckBoxClicked(it))
                    })
                Spacer(modifier = Modifier.height(20.dp))
                ButtonComponent(value = "Register", onButtonClicked = {
                    signUpViewModel.onEvent(UIEvent.RegisterButtonClicked)
                } , isEnabled = signUpViewModel.allValuePassed.value)
                DividerTextComponent()
                ClickableLoginTextComponent(true) {
                    PostOfficeAppRouter.navigateTo(Screen.LoginScreen)
                }
            }
        }
        if(signUpViewModel.signUpInProgress.value){
        CircularProgressIndicator()
        }
    }


}