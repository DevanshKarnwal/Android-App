package com.example.firebaseproject1.data.rules

object Validator {
    fun validateFirstName(fName : String):ValidationResult{
        return ValidationResult(
            (!fName.isNullOrEmpty() && fName.length >= 2)
        )
    }
    fun validateLastName(lName : String) :ValidationResult {
        return ValidationResult(
            (!lName.isNullOrEmpty() && lName.length >= 2)
        )
    }
    fun validateEmail(Email : String) :ValidationResult {
        return ValidationResult(
            (!Email.isNullOrEmpty() )
        )
    }
    fun validatePassword(Password : String) :ValidationResult {
        return ValidationResult(
            (!Password.isNullOrEmpty() && Password.length >= 4)
        )
    }
    fun validatePrivacyPolicyAcceptance (statusValue : Boolean) : ValidationResult {
        return ValidationResult(statusValue)
    }
}

data class ValidationResult(
    val status : Boolean = false,
)