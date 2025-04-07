package com.example.mediaapp.backend.auth

import kotlinx.coroutines.flow.Flow

sealed class AuthResult<out T> {
    data class Success<out T>(val data: T) : AuthResult<T>()
    data class Error(val message: String) : AuthResult<Nothing>()
}

// Simple class to hold basic user info (adapt as needed)
data class AuthUser(val uid: String, val email: String?, val displayName: String?)

interface AuthRepository {
    // Function to get the current user state (e.g., logged in or not)
    // Using Flow allows observing auth state changes
    fun getAuthState(): Flow<AuthUser?>

    // Function to get the current user synchronously if needed (might return null)
    suspend fun getCurrentUser(): AuthUser?

    // Login function
    suspend fun loginUser(email: String, password: String): AuthResult<AuthUser>

    // Registration function
    suspend fun registerUser(email: String, password: String, username: String): AuthResult<AuthUser>

    // Password reset function
    suspend fun sendPasswordReset(email: String): AuthResult<Unit>

    // Logout function
    suspend fun logout(): AuthResult<Unit>
}