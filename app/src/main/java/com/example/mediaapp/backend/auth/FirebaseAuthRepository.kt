package com.example.mediaapp.backend.auth

import com.example.mediaapp.backend.database.DatabaseHandler
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class FirebaseAuthRepository(
    private val firebaseAuth: FirebaseAuth = Firebase.auth,
    private val databaseHandler: DatabaseHandler = DatabaseHandler.getInstance()
) : AuthRepository {

    override fun getAuthState(): Flow<AuthUser?> = callbackFlow {
        val authStateListener = FirebaseAuth.AuthStateListener { auth ->
            trySend(auth.currentUser?.toAuthUser())
        }
        firebaseAuth.addAuthStateListener(authStateListener)
        awaitClose { firebaseAuth.removeAuthStateListener(authStateListener) }
    }

    override suspend fun getCurrentUser(): AuthUser? {
        return firebaseAuth.currentUser?.toAuthUser()
    }

    override suspend fun loginUser(email: String, password: String): AuthResult<AuthUser> {
        return try {
            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            result.user?.toAuthUser()?.let {
                AuthResult.Success(it)
            } ?: AuthResult.Error("Login failed: User data unavailable.")
        } catch (e: Exception) {
            AuthResult.Error(e.localizedMessage ?: "An unknown login error occurred.")
        }
    }

    override suspend fun registerUser(email: String, password: String, username: String): AuthResult<AuthUser> {
        return try {
            val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            val firebaseUser = result.user
            if (firebaseUser != null) {
                // Update profile
                val profileUpdates = UserProfileChangeRequest.Builder()
                    .setDisplayName(username)
                    .build()
                firebaseUser.updateProfile(profileUpdates).await()

                val userMap = createUserMap(username)
                databaseHandler.updateUserInDatabase(firebaseUser.uid, userMap)

                firebaseUser.toAuthUser()?.let {
                    AuthResult.Success(it)
                } ?: AuthResult.Error("Registration succeeded but user data is null.")
            } else {
                AuthResult.Error("Registration failed: User creation returned null.")
            }
        } catch (e: Exception) {
            AuthResult.Error(e.localizedMessage ?: "An unknown registration error occurred.")
        }
    }

    override suspend fun sendPasswordReset(email: String): AuthResult<Unit> {
        return try {
            firebaseAuth.sendPasswordResetEmail(email).await()
            AuthResult.Success(Unit)
        } catch (e: Exception) {
            AuthResult.Error(e.localizedMessage ?: "Password reset failed.")
        }
    }

    override suspend fun logout(): AuthResult<Unit> {
        return try {
            firebaseAuth.signOut()
            AuthResult.Success(Unit)
        } catch (e: Exception) {
            // Signing out shouldn't really throw exceptions, but handle just in case
            AuthResult.Error(e.localizedMessage ?: "Logout failed.")
        }
    }

    // Helper to convert FirebaseUser to AuthUser model
    private fun com.google.firebase.auth.FirebaseUser.toAuthUser(): AuthUser? {
        return AuthUser(
            uid = this.uid,
            email = this.email,
            displayName = this.displayName
        )
    }

    // Helper function to create a user map for Firestore
    private fun createUserMap(username: String) = hashMapOf(
        "username" to username,
        "name" to "",
        "location" to "",
        "followers" to listOf<String>(),
        "following" to listOf<String>(),
        "description" to "",
        "profilePicture" to "",
        "stats" to hashMapOf(
            "watched" to 0,
            "reviews" to 0,
            "rated" to 0,
            "recommends" to 0,
            "saved" to 0
        ),
        "watchlist" to listOf<String>()
    )
}