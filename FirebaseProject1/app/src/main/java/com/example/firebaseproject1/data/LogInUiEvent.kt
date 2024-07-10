package com.example.firebaseproject1.data

sealed class LogInUiEvent {
    data class EmailChanged(val email: String) : LogInUiEvent()
    data class PasswordChanged(val password: String) : LogInUiEvent()
    object LogInButtonClicked : LogInUiEvent()


}