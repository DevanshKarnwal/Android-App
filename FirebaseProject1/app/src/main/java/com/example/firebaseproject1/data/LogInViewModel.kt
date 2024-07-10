package com.example.firebaseproject1.data

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.firebaseproject1.data.rules.Validator
import com.google.firebase.auth.FirebaseAuth

class LogInViewModel : ViewModel() {
    var loginUiState = mutableStateOf(LoginUiState())
    var allValidatonPassed = mutableStateOf(false)
    var logInInProgress = mutableStateOf(false)

    fun onEvent(event: LogInUiEvent) {
        when (event) {

            is LogInUiEvent.EmailChanged -> {
                loginUiState.value = loginUiState.value.copy(email = event.email)
            }

            is LogInUiEvent.PasswordChanged -> {
                loginUiState.value = loginUiState.value.copy(password = event.password)
            }

            is LogInUiEvent.LogInButtonClicked -> {
                logIn()
            }
        }
        validateLoginUIDataWithRules()
    }
    fun logOut(){
        val firebaseAuth = FirebaseAuth.getInstance()
        firebaseAuth.signOut()
    }
    private fun logIn() {
        logInInProgress.value = true
        FirebaseAuth
            .getInstance()
            .signInWithEmailAndPassword(loginUiState.value.email, loginUiState.value.password)
            .addOnCompleteListener {
                logInInProgress.value = false
                Log.d("LoginViewModel", "isSuccessful ${it.isSuccessful}")
            }
            .addOnFailureListener{
                Log.d("LoginViewModel", "isFaliureLogin ${it.message}")
            }
    }
    private fun validateLoginUIDataWithRules() {
        val emailResult = Validator.validateEmail(loginUiState.value.email)
        val passwordResult = Validator.validatePassword(loginUiState.value.password)
        loginUiState.value = loginUiState.value.copy(
            emailError = emailResult.status,
            passwordError = passwordResult.status
        )
        allValidatonPassed.value = emailResult.status && passwordResult.status
    }
}