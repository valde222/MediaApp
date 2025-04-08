package com.example.mediaapp.backend.database

import com.example.mediaapp.models.CurrentUser
import com.example.mediaapp.models.RatingForDatabase
import com.example.mediaapp.models.Recommend
import com.example.mediaapp.models.User // Assuming User is needed, adjust if only CurrentUser is sufficient
import com.example.mediaapp.models.WatchlistMovie

// Consider adding Result types (like AuthResult) for better error handling if needed

interface DatabaseRepository {

    // User Profile Operations
    suspend fun getUserProfile(): CurrentUser?
    suspend fun updateUserProfile(userId: String, userMap: Map<String, Any?>)
    suspend fun updateUserProfileDetails(updatedUser: User)

    // Watched List Operations
    suspend fun getWatchedMovies(): List<WatchlistMovie>
    suspend fun addWatchedMovie(watchedMovieMap: Map<String, Any?>)
    suspend fun removeWatchedMovie(movieId: Long)

    // Watchlist Operations
    suspend fun getWatchlistMovies(): List<WatchlistMovie>
    suspend fun addOrUpdateWatchlistMovie(watchlistMovieMap: Map<String, Any?>)
    suspend fun removeWatchlistMovie(movieId: Long)

    // Rating Operations
    suspend fun getUserRatings(): List<RatingForDatabase>
    suspend fun getMovieRatings(movieId: String): List<RatingForDatabase>
    suspend fun addOrUpdateUserRating(movieId: String, rating: RatingForDatabase)
    suspend fun addOrUpdateMovieRating(movieId: String, rating: RatingForDatabase) // Need userId here

    // Recommendation Operations
    suspend fun getRecommendedMovies(): List<Recommend>
    suspend fun addRecommendedMovie(recommendMovieMap: Map<String, Any?>)
    suspend fun removeRecommendedMovie(movieId: Long)

}