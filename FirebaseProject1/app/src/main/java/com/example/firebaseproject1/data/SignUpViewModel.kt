package com.example.firebaseproject1.data

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.firebaseproject1.data.rules.Validator
import com.example.firebaseproject1.data.rules.Validator.validateFirstName
import com.example.firebaseproject1.navigation.PostOfficeAppRouter
import com.example.firebaseproject1.navigation.Screen
import com.google.firebase.auth.FirebaseAuth

class SignUpViewModel : ViewModel(){
    var registrationUIState = mutableStateOf(RegistrationUIState())
    val allValuePassed = mutableStateOf(false)
    var signUpInProgress = mutableStateOf(false)
    fun onEvent(event : UIEvent){
        validateDataWithRules()
        when(event){
            is UIEvent.FirstNameChanged ->{
                registrationUIState.value = registrationUIState.value.copy(firstName = event.firstName)

            }
            is UIEvent.LastNameChanged -> {
                registrationUIState.value =
                    registrationUIState.value.copy(lastName = event.lastName)
            }
            is UIEvent.EmailChanged -> {
                registrationUIState.value =
                    registrationUIState.value.copy(email = event.email)
            }
            is UIEvent.PasswordChanged -> {
                registrationUIState.value =
                    registrationUIState.value.copy(password = event.password)
            }
            is UIEvent.PrivacyPolicyCheckBoxClicked ->{
                registrationUIState.value =
                    registrationUIState.value.copy(privacyPolicyAccepted = event.status)
            }
            is UIEvent.RegisterButtonClicked ->{
                signUp()
            }
        }
    }
    private fun signUp() {
        createUserInFirebase(registrationUIState.value.email, registrationUIState.value.password)
    }

    private fun logOut(){
        val firebaseAuth = FirebaseAuth.getInstance()
        firebaseAuth.signOut()
        val authStateListener = FirebaseAuth.AuthStateListener {
            if(it.currentUser == null){
                Log.d("SignUpViewModel", "User Logged Out")
            }
            else{
                Log.d("SignUpViewModel", "User Logged out not complete")
            }
        }
        firebaseAuth.addAuthStateListener(authStateListener)
    }

    private fun validateDataWithRules() {
        val firstNameResult = Validator.validateFirstName(registrationUIState.value.firstName)
        val lastNameResult = Validator.validateLastName(registrationUIState.value.lastName)
        val emailResult = Validator.validateEmail(registrationUIState.value.email)
        val passwordResult = Validator.validatePassword(registrationUIState.value.password)
        val privacyPolicyResult = Validator.validatePrivacyPolicyAcceptance(statusValue = registrationUIState.value.privacyPolicyAccepted)
        registrationUIState.value = registrationUIState.value.copy(
            firstNameError = firstNameResult.status,
            lastNameError = lastNameResult.status,
            emailError = emailResult.status,
            passwordError = passwordResult.status,
            privacyPolicyError = privacyPolicyResult.status
        )
        if (firstNameResult.status && lastNameResult.status && emailResult.status && passwordResult.status && privacyPolicyResult.status) {
            allValuePassed.value = true
        } else {
            allValuePassed.value = false
        }
    }
    private fun createUserInFirebase(email: String, password: String){
        signUpInProgress.value = true
        FirebaseAuth
            .getInstance()
            .createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener{
                Log.d("C", "Insidie on CompleteListner ")
                Log.d("LoginViewModel", "isSuccessful ${it.isSuccessful} ")
                signUpInProgress.value = false
                if(it.isSuccessful){
                    PostOfficeAppRouter.navigateTo(Screen.HomeScreen)
                }
            }
            .addOnFailureListener{
                Log.d("LoginViewModel","Inside OnFailureListener" )
                Log.d("LoginViewModel", "Exception ${it.message} ")

            }
    }

}