package com.example.firebasesignin.presentation

data class SignInState(
    val isSignInSuccessful : Boolean = false,
    val signInError : String? = null
)
