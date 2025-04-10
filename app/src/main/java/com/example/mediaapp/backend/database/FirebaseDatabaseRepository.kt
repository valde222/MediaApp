package com.example.mediaapp.backend.database

import android.util.Log
import com.example.mediaapp.backend.auth.AuthRepository
import com.example.mediaapp.backend.auth.FirebaseAuthRepository
import com.example.mediaapp.models.CurrentUser
import com.example.mediaapp.models.RatingForDatabase
import com.example.mediaapp.models.Recommend
import com.example.mediaapp.models.User
import com.example.mediaapp.models.WatchlistMovie
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class FirebaseDatabaseRepository(
    private val firestore: FirebaseFirestore = Firebase.firestore,
    private val authRepository: AuthRepository = FirebaseAuthRepository()
) : DatabaseRepository {

    private var userCache: CurrentUser? = null
    private var watchListCache: List<WatchlistMovie>? = null
    private var watchedListCache: List<WatchlistMovie>? = null
    private var recommendCache: List<Recommend>? = null

    private suspend fun getCurrentUserID(): String? {
        return authRepository.getCurrentUser()?.uid
    }

    // --- User Profile ---
    override suspend fun getUserProfile(): CurrentUser? = withContext(Dispatchers.IO) {
        val userId = getCurrentUserID() ?: return@withContext null
        if (userCache != null) { // Basic cache check
            Log.d("DB_REPO", "Returning cached user data")
            return@withContext userCache
        }

        try {
            val userDataSnapshot = firestore.collection("users").document(userId).get().await()
            val userMap = userDataSnapshot.data
            if (userMap != null) {
                val user = User.fromMap(userMap)
                // Fetch watchlist separately or assume it's part of the User map if structured differently
                val watchlist = getWatchlistMovies() // Or fetch specific field if stored directly
                val currentUser = CurrentUser(user, watchlist)
                userCache = currentUser // Update cache
                Log.d("DB_REPO", "Fetched user profile for $userId")
                currentUser
            } else {
                Log.w("DB_REPO", "User profile not found for $userId")
                null
            }
        } catch (e: Exception) {
            Log.e("DB_REPO", "Error fetching user profile for $userId", e)
            null
        }
    }

    // Use this when registering a new user or replacing the whole document
    override suspend fun updateUserProfile(userId: String, userMap: Map<String, Any?>): Unit = withContext(Dispatchers.IO + NonCancellable) {
        try {
            firestore.collection("users").document(userId).set(userMap).await()
            userCache = null // Invalidate cache
            Log.d("DB_REPO", "Updated user profile for $userId")
        } catch (e: Exception) {
            Log.e("DB_REPO", "Error updating user profile for $userId", e)
            // Handle error appropriately
        }
    }

    // Use this for partial updates (like in CurrentUserViewModel)
    override suspend fun updateUserProfileDetails(updatedUser: User) = withContext(Dispatchers.IO) {
        val userId = getCurrentUserID() ?: return@withContext
        try {
            // Convert User back to Map for Firestore update
            // Make sure User.toMap() exists and works correctly
            val userMap = User.toMap(updatedUser)
            firestore.collection("users").document(userId).update(userMap).await()
            userCache = null // Invalidate cache
            Log.d("DB_REPO", "Updated user profile details for $userId")
        } catch (e: Exception) {
            Log.e("DB_REPO", "Error updating user profile details for $userId", e)
        }
    }

    // --- Watched List ---
    override suspend fun getWatchedMovies(): List<WatchlistMovie> = withContext(Dispatchers.IO) {
        val userId = getCurrentUserID() ?: return@withContext emptyList()
        if (watchedListCache != null) return@withContext watchedListCache!! // Return cache if valid

        try {
            val snapshot = firestore.collection("users").document(userId)
                .collection("watched").get().await()
            val movies = snapshot.documents.mapNotNull { WatchlistMovie.fromMap(it.data ?: emptyMap()) }
            watchedListCache = movies // Update cache
            Log.d("DB_REPO", "Fetched ${movies.size} watched movies for $userId")
            movies
        } catch (e: Exception) {
            Log.e("DB_REPO", "Error fetching watched movies for $userId", e)
            emptyList()
        }
    }

    override suspend fun addWatchedMovie(watchedMovieMap: Map<String, Any?>) = withContext(Dispatchers.IO) {
        val userId = getCurrentUserID() ?: return@withContext
        val movieId = watchedMovieMap["movieID"]?.toString() ?: return@withContext

        try {
            firestore.collection("users").document(userId)
                .collection("watched").document(movieId).set(watchedMovieMap).await()
            watchedListCache = null // Invalidate cache
            Log.d("DB_REPO", "Added watched movie $movieId for $userId")
        } catch (e: Exception) {
            Log.e("DB_REPO", "Error adding watched movie $movieId for $userId", e)
        }
    }

    override suspend fun removeWatchedMovie(movieId: Long) = withContext(Dispatchers.IO) {
        val userId = getCurrentUserID() ?: return@withContext
        try {
            firestore.collection("users").document(userId)
                .collection("watched").document(movieId.toString()).delete().await()
            watchedListCache = null // Invalidate cache
            Log.d("DB_REPO", "Removed watched movie $movieId for $userId")
        } catch (e: Exception) {
            Log.e("DB_REPO", "Error removing watched movie $movieId for $userId", e)
        }
    }


    // --- Watchlist ---
    override suspend fun getWatchlistMovies(): List<WatchlistMovie> = withContext(Dispatchers.IO) {
        val userId = getCurrentUserID() ?: return@withContext emptyList()
        if (watchListCache != null) return@withContext watchListCache!! // Return cache

        try {
            val snapshot = firestore.collection("users").document(userId)
                .collection("watchlist").get().await()
            val movies = snapshot.documents.mapNotNull { WatchlistMovie.fromMap(it.data ?: emptyMap()) }
            watchListCache = movies // Update cache
            Log.d("DB_REPO", "Fetched ${movies.size} watchlist movies for $userId")
            movies
        } catch (e: Exception) {
            Log.e("DB_REPO", "Error fetching watchlist movies for $userId", e)
            emptyList()
        }
    }

    override suspend fun addOrUpdateWatchlistMovie(watchlistMovieMap: Map<String, Any?>) = withContext(Dispatchers.IO) {
        val userId = getCurrentUserID() ?: return@withContext
        val movieId = watchlistMovieMap["movieID"]?.toString() ?: return@withContext

        try {
            firestore.collection("users").document(userId)
                .collection("watchlist").document(movieId).set(watchlistMovieMap).await()
            watchListCache = null // Invalidate cache
            // Also remove from recommendations if added to watchlist
            removeRecommendedMovieInternal(userId, movieId.toLong())
            Log.d("DB_REPO", "Added/Updated watchlist movie $movieId for $userId")
        } catch (e: Exception) {
            Log.e("DB_REPO", "Error adding/updating watchlist movie $movieId for $userId", e)
        }
    }

    override suspend fun removeWatchlistMovie(movieId: Long) = withContext(Dispatchers.IO) {
        val userId = getCurrentUserID() ?: return@withContext
        try {
            firestore.collection("users").document(userId)
                .collection("watchlist").document(movieId.toString()).delete().await()
            watchListCache = null // Invalidate cache
            Log.d("DB_REPO", "Removed watchlist movie $movieId for $userId")
        } catch (e: Exception) {
            Log.e("DB_REPO", "Error removing watchlist movie $movieId for $userId", e)
        }
    }


    // --- Ratings ---
    override suspend fun getUserRatings(): List<RatingForDatabase> = withContext(Dispatchers.IO) {
        val userId = getCurrentUserID() ?: return@withContext emptyList()
        try {
            val snapshot = firestore.collection("users").document(userId)
                .collection("ratedMovies").get().await()
            val ratings = snapshot.documents.mapNotNull { RatingForDatabase.fromMap(it.data ?: emptyMap()) }
            Log.d("DB_REPO", "Fetched ${ratings.size} user ratings for $userId")
            ratings
        } catch (e: Exception) {
            Log.e("DB_REPO", "Error fetching user ratings for $userId", e)
            emptyList()
        }
    }

    override suspend fun getMovieRatings(movieId: String): List<RatingForDatabase> = withContext(Dispatchers.IO) {
        try {
            val snapshot = firestore.collection("ratedMovies").document(movieId)
                .collection("ratings").get().await()
            val ratings = snapshot.documents.mapNotNull { RatingForDatabase.fromMap(it.data ?: emptyMap()) }
            Log.d("DB_REPO", "Fetched ${ratings.size} ratings for movie $movieId")
            ratings
        } catch (e: Exception) {
            Log.e("DB_REPO", "Error fetching ratings for movie $movieId", e)
            emptyList()
        }
    }

    override suspend fun addOrUpdateUserRating(movieId: String, rating: RatingForDatabase) = withContext(Dispatchers.IO) {
        val userId = getCurrentUserID() ?: return@withContext
        try {
            firestore.collection("users").document(userId)
                .collection("ratedMovies").document(movieId).set(rating).await()
            Log.d("DB_REPO", "Added/Updated user rating for movie $movieId for $userId")
        } catch (e: Exception) {
            Log.e("DB_REPO", "Error adding/updating user rating for $movieId for $userId", e)
        }
    }

    override suspend fun addOrUpdateMovieRating(movieId: String, rating: RatingForDatabase): Unit = withContext(Dispatchers.IO) {
        val userId = getCurrentUserID() ?: return@withContext
        try {
            firestore.collection("ratedMovies").document(movieId)
                .collection("ratings").document(userId).set(rating).await()
            Log.d("DB_REPO", "Added/Updated movie rating for movie $movieId by user $userId")
        } catch (e: Exception) {
            Log.e("DB_REPO", "Error adding/updating movie rating for $movieId by user $userId", e)
        }
    }

    // --- Recommendations ---
    override suspend fun getRecommendedMovies(): List<Recommend> = withContext(Dispatchers.IO) {
        val userId = getCurrentUserID() ?: return@withContext emptyList()
        if (recommendCache != null) return@withContext recommendCache!! // Return cache

        try {
            val snapshot = firestore.collection("users").document(userId)
                .collection("recommend").get().await()
            val movies = snapshot.documents.mapNotNull { Recommend.fromMap(it.data ?: emptyMap()) }
            recommendCache = movies // Update cache
            Log.d("DB_REPO", "Fetched ${movies.size} recommended movies for $userId")
            movies
        } catch (e: Exception) {
            Log.e("DB_REPO", "Error fetching recommended movies for $userId", e)
            emptyList()
        }
    }

    override suspend fun addRecommendedMovie(recommendMovieMap: Map<String, Any?>) = withContext(Dispatchers.IO) {
        val userId = getCurrentUserID() ?: return@withContext
        val movieId = recommendMovieMap["movieID"]?.toString() ?: return@withContext

        try {
            firestore.collection("users").document(userId)
                .collection("recommend").document(movieId).set(recommendMovieMap).await()
            recommendCache = null // Invalidate cache
            Log.d("DB_REPO", "Added recommended movie $movieId for $userId")
        } catch (e: Exception) {
            Log.e("DB_REPO", "Error adding recommended movie $movieId for $userId", e)
        }
    }

    override suspend fun removeRecommendedMovie(movieId: Long) = withContext(Dispatchers.IO) {
        val userId = getCurrentUserID() ?: return@withContext
        removeRecommendedMovieInternal(userId, movieId)
    }

    // Internal helper to avoid context switching when called from another method
    private suspend fun removeRecommendedMovieInternal(userId: String, movieId: Long) {
        try {
            firestore.collection("users").document(userId)
                .collection("recommend").document(movieId.toString()).delete().await()
            recommendCache = null // Invalidate cache
            Log.d("DB_REPO", "Removed recommended movie $movieId for $userId")
        } catch (e: Exception) {
            Log.e("DB_REPO", "Error removing recommended movie $movieId for $userId", e)
        }
    }

}