package com.example.noteappusingfirebase.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun LoginScreen(
    loginViewModel: LoginViewModel? = null,
    onNavToHomepage :() ->Unit,
    onNavToSignUpage :() ->Unit,
) {
    val loginUiState = loginViewModel?.loginUiState
    val isError = loginUiState?.loginError != null
    val context = LocalContext.current
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
//        verticalArrangement = Arrangement.Center
    ){
            Text(
                text = "Login",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Black,
                color = MaterialTheme.colorScheme.primary,
                )
            if(isError){
                Text(text = loginUiState?.loginError ?:"unknown Error" , color = Color.Red)
            }
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            value = loginUiState?.userName?:" ",
            onValueChange = {loginViewModel?.onUserNameChange(it)},
            leadingIcon = {
                Icon(imageVector = Icons.Default.Person,
                    contentDescription = null)
            },
            label = { Text(text = "Email")},
            isError = isError
            )
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            value = loginUiState?.password?:" ",
            onValueChange = {loginViewModel?.onPasswordChange(it)},
            leadingIcon = {
                Icon(imageVector = Icons.Default.Lock,
                    contentDescription = null)
            },
            label = { Text(text = "Password")},
            visualTransformation = PasswordVisualTransformation(),
            isError = isError
        )
        Button(onClick = { loginViewModel?.loginUser(context) }) {
            Text(text = "Signin")
        }
        Spacer(modifier = Modifier.size(16.dp))
        Row(
            modifier= Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Don't have an account")
            Spacer(modifier = Modifier.size(8.dp))
            TextButton(onClick = { onNavToSignUpage() } ){
                Text(text = "Sign up")
            }
        }

        if(loginUiState?.isLoadding == true ){
            CircularProgressIndicator()
        }

        LaunchedEffect(key1 = loginViewModel?.hasUser) {
            if(loginViewModel?.hasUser == true){
                onNavToHomepage()
            }
        }

    }
}


@Composable
fun SignUpScreen(
    loginViewModel: LoginViewModel? = null,
    onNavToHomepage :() ->Unit,
    onNavToLoginpage :() ->Unit,
) {
    val loginUiState = loginViewModel?.loginUiState
    val isError = loginUiState?.signUpError != null
    val context = LocalContext.current
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
//        verticalArrangement = Arrangement.Center
    ){
        Text(
            text = "SignUp",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Black,
            color = MaterialTheme.colorScheme.primary,
        )
        if(isError){
            Text(text = loginUiState?.signUpError ?:"unknown Error" , color = Color.Red)
        }
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            value = loginUiState?.userNameSignUp?:" ",
            onValueChange = {loginViewModel?.onUserNameChangeSignUp(it)},
            leadingIcon = {
                Icon(imageVector = Icons.Default.Person,
                    contentDescription = null)
            },
            label = { Text(text = "Email")},
            isError = isError
        )
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            value = loginUiState?.passwordSignUp?:"",
            onValueChange = {loginViewModel?.passwordChangedSignUp(it)},
            leadingIcon = {
                Icon(imageVector = Icons.Default.Lock,
                    contentDescription = null)
            },
            label = { Text(text = "Password")},
            visualTransformation = PasswordVisualTransformation(),
            isError = isError
        )
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            value = loginUiState?.confirmPasswordSignUp?:"",
            onValueChange = {loginViewModel?.onConfirmPasswordChange(it)},
            leadingIcon = {
                Icon(imageVector = Icons.Default.Lock,
                    contentDescription = null)
            },
            label = { Text(text = "Confirm Password")},
            visualTransformation = PasswordVisualTransformation(),
            isError = isError
        )
        Button(onClick = { loginViewModel?.createUser(context) }) {
            Text(text = "Signin")
        }
        Spacer(modifier = Modifier.size(16.dp))
        Row(
            modifier= Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Already have an account")
            Spacer(modifier = Modifier.size(8.dp))
            TextButton(onClick = { onNavToLoginpage() } ){
                Text(text = "Sign In")
            }
        }

        if(loginUiState?.isLoadding == true ){
            CircularProgressIndicator()
        }

        LaunchedEffect(key1 = loginViewModel?.hasUser) {
            if(loginViewModel?.hasUser == true){
                onNavToHomepage()
            }
        }

    }
}


@Preview(showSystemUi = true)
@Composable
fun LoginScreenPreview() {
    LoginScreen(onNavToHomepage = {}, onNavToSignUpage = {})
}
@Preview(showSystemUi = true)
@Composable
fun SignUpScreenPreview() {
    SignUpScreen(onNavToHomepage = {}, onNavToLoginpage = {})
}