package com.example.mediaapp.viewModels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.mediaapp.Screen
import com.example.mediaapp.backend.auth.AuthRepository
import com.example.mediaapp.backend.auth.AuthResult
import kotlinx.coroutines.launch

/**
 * ViewModel for the login page.
 * Handles user registration and input validation.
 */
class LoginPageViewModel(private val authRepository: AuthRepository) : ViewModel() {

    // Variables to hold user input
    var errorText = mutableStateOf("")
    var email = ""
    var password = ""
    var confirmPassword = ""
    var username = ""

    val authState = authRepository.getAuthState()

    fun registerFlow(navController: NavController) {
        try {
            validateInput(true)
            if (errorText.value.isEmpty()) {
                viewModelScope.launch {
                    when (val result = authRepository.registerUser(email, password, username)) {
                        is AuthResult.Success -> {
                            // Registration successful, navigate to login or main screen
                            errorText.value = "User created successfully"
                            navController.navigate(Screen.Login.route) {
                                popUpTo(Screen.Register.route) { inclusive = true }
                            }
                        }
                        is AuthResult.Error -> {
                            // Handle specific errors if needed based on result.message
                            errorText.value = mapFirebaseErrorToUserMessage(result.message)
                        }
                    }
                }
            }
        } catch (e: IllegalArgumentException) {
            errorText.value = e.message ?: "Validation failed"
        }
    }

    fun loginFlow(navController: NavController) {
        try {
            validateInput(false)
            if (errorText.value.isEmpty()) {
                viewModelScope.launch {
                    when (val result = authRepository.loginUser(email, password)) {
                        is AuthResult.Success -> {
                            // Login successful, navigate to main screen
                            errorText.value = ""
                            navController.navigate(Screen.MainScreen.route) {
                                popUpTo(Screen.Login.route) { inclusive = true }
                            }
                        }
                        is AuthResult.Error -> {
                            errorText.value = mapFirebaseErrorToUserMessage(result.message)
                        }
                    }
                }
            }
        } catch (e: IllegalArgumentException) {
            errorText.value = e.message ?: "Validation failed"
        }
    }

    fun sendPasswordResetEmail(navController: NavController) {
        if (email.isEmpty()) {
            errorText.value = ERROR_EMPTY_FIELDS
        } else {
            viewModelScope.launch {
                when(val result = authRepository.sendPasswordReset(email)) {
                    is AuthResult.Success -> {
                        errorText.value = "Password reset email sent"
                        navController.navigate(Screen.Login.route)
                    }
                    is AuthResult.Error -> {
                        errorText.value = mapFirebaseErrorToUserMessage(result.message)
                    }
                }
            }
        }
    }


    companion object {
        const val ERROR_EMPTY_FIELDS = "Please fill in all fields"
        const val ERROR_INVALID_EMAIL = "Please enter a valid email address"
        const val ERROR_SHORT_PASSWORD = "Password must be at least 8 characters"
        const val ERROR_PASSWORD_MISMATCH = "Password does not match"
        const val ERROR_WRONG_LENGTH_USERNAME = "Username must be between 4 and 20 characters"
        const val ERROR_USERNAME_SPECIAL_CHARACTERS_SPACES = "Username cannot contain spaces or special characters"
        const val ERROR = "Failed, try again"
    }

    private fun validateInput(registration: Boolean) {
        if (email.isEmpty() || password.isEmpty()) throw IllegalArgumentException(ERROR_EMPTY_FIELDS)
        if (!email.contains("@") || !email.contains(".")) throw IllegalArgumentException(ERROR_INVALID_EMAIL)
        if (password.length < 8) throw IllegalArgumentException(ERROR_SHORT_PASSWORD)
        if (registration) {
            if (username.isEmpty() || confirmPassword.isEmpty()) throw IllegalArgumentException(ERROR_EMPTY_FIELDS)
            if (password != confirmPassword) throw IllegalArgumentException(ERROR_PASSWORD_MISMATCH)
            if (username.length !in 4..20) throw IllegalArgumentException(ERROR_WRONG_LENGTH_USERNAME)
            if (username.contains(" ") || username.any { it in "@.#\$[]/\\%^&*()+=?!<>,;:\"{}|~`'" }) throw IllegalArgumentException(ERROR_USERNAME_SPECIAL_CHARACTERS_SPACES)
        }
        errorText.value = ""
    }

    private fun mapFirebaseErrorToUserMessage(firebaseError: String): String {
        return when {
            firebaseError.contains("ERROR_INVALID_EMAIL", ignoreCase = true) || firebaseError.contains("email address is badly formatted", ignoreCase = true) -> ERROR_INVALID_EMAIL
            firebaseError.contains("ERROR_WRONG_PASSWORD", ignoreCase = true) || firebaseError.contains("supplied auth credential is incorrect", ignoreCase = true) -> "The email or password is incorrect"
            firebaseError.contains("ERROR_USER_NOT_FOUND", ignoreCase = true) || firebaseError.contains("no user record", ignoreCase = true) -> "No account found with this email"
            firebaseError.contains("ERROR_EMAIL_ALREADY_IN_USE", ignoreCase = true) || firebaseError.contains("email address is already in use", ignoreCase = true) -> "This email address is already in use"
            firebaseError.contains("ERROR_WEAK_PASSWORD", ignoreCase = true) -> ERROR_SHORT_PASSWORD // Firebase might have a different message
            firebaseError.contains("temporarily disabled", ignoreCase = true) -> "Too many failed login attempts. Please try again later or reset your password."
            else -> firebaseError // Return the original error if not mapped
        }
    }
}
