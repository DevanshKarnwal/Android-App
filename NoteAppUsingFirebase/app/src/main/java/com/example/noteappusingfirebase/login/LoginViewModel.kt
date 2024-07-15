package com.example.noteappusingfirebase.login

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.noteappusingfirebase.repository.AuthRepository
import kotlinx.coroutines.launch

class LoginViewModel (
    private val repository: AuthRepository = AuthRepository()
) : ViewModel() {

    val currentUser = repository.currentUser

    val hasUser : Boolean
    get() = repository.hasUser()

    var loginUiState by mutableStateOf(LoginUiState())
        private set

    fun onUserNameChange(userName: String){
        loginUiState = loginUiState.copy(userName = userName)
    }

    fun onPasswordChange(password: String){
        loginUiState = loginUiState.copy(password = password)
    }

    fun onUserNameChangeSignUp(userNameSignUp: String){
        loginUiState = loginUiState.copy(userNameSignUp = userNameSignUp)
    }

    fun passwordChangedSignUp(password: String){
        loginUiState = loginUiState.copy(passwordSignUp = password)
    }

    fun onConfirmPasswordChange(password: String){
        loginUiState = loginUiState.copy(confirmPasswordSignUp = password)
    }

    private fun validateLoginForm() =
        loginUiState.userName.isNotBlank() && loginUiState.password.isNotBlank()

    private fun validateSignUpForm() =
        loginUiState.userNameSignUp.isNotBlank() && loginUiState.passwordSignUp.isNotBlank()
                && loginUiState.confirmPasswordSignUp.isNotBlank()


    fun createUser(context: Context) = viewModelScope.launch {
        try {
            if (!validateSignUpForm()) {
                throw IllegalArgumentException("Email and password can't be empty")
            }

            loginUiState = loginUiState.copy(isLoadding = true)

            if (loginUiState.passwordSignUp != loginUiState.confirmPasswordSignUp) {
                throw IllegalArgumentException("Passwords don't match")
            }

            loginUiState = loginUiState.copy(signUpError = null)

            repository.createUser(loginUiState.userNameSignUp, loginUiState.passwordSignUp) { isSuccessful ->
                if (isSuccessful) {
                    Toast.makeText(context, "Success login", Toast.LENGTH_LONG).show()
                    loginUiState = loginUiState.copy(isSuccessfulLogin = true)
                } else {
                    Toast.makeText(context, "Fail login", Toast.LENGTH_LONG).show()
                    loginUiState = loginUiState.copy(isSuccessfulLogin = false)
                }
            }

        } catch (e: Exception) {
            loginUiState = loginUiState.copy(signUpError = e.localizedMessage)
            e.printStackTrace()
        } finally {
            loginUiState = loginUiState.copy(isLoadding = false)
        }
    }

    fun loginUser(context : Context) = viewModelScope.launch {
        try{
            if(!validateLoginForm()){
                throw IllegalArgumentException("Email and password can't be empty")
                loginUiState = loginUiState.copy(isLoadding = true)

                loginUiState = loginUiState.copy(loginError = null)

                repository.login(loginUiState.userName
                    ,loginUiState.password){
                        isSuccessful -> if(isSuccessful){
                    Toast.makeText(context, "Success login ", Toast.LENGTH_LONG).show()
                    loginUiState = loginUiState.copy(isSuccessfulLogin = true)
                }
                else{
                    Toast.makeText(context, "Fail login ", Toast.LENGTH_LONG).show()
                    loginUiState = loginUiState.copy(isSuccessfulLogin = false)

                }
                }

            }
        }catch (e : Exception){
            loginUiState = loginUiState.copy(loginError = e.localizedMessage)
            e.printStackTrace()
        }
        finally{
            loginUiState = loginUiState.copy(isLoadding = false)
        }
    }



}

data class LoginUiState(
    val userName : String = "",
    val password : String = "",
    val userNameSignUp : String = "",
    val passwordSignUp : String = "",
    val confirmPasswordSignUp : String = "",
    val isLoadding : Boolean = false,
    val isSuccessfulLogin : Boolean = false,
    val signUpError : String? = null,
    val loginError : String? = null

    )