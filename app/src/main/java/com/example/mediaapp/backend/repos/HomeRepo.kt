package com.example.mediaapp.backend.repos

import android.util.Log
import com.example.mediaapp.backend.RecommendationEngine
import com.example.mediaapp.backend.apirequests.APIHandler
import com.example.mediaapp.models.Recommend
import com.example.mediaapp.models.Result2
import com.example.mediaapp.models.TMDBMovie

class HomeRepo(
    private val recommendationEngine: RecommendationEngine
) {
    private val apiHandler = APIHandler()
    private var popularMoviesCache: List<TMDBMovie>? = null
    private var moviesInTheatreCache: List<Result2>? = null
    private var upcomingMoviesCache: List<Result2>? = null

    suspend fun getPopularMovies(timeWindow: String): Result<List<TMDBMovie>?> {
        return if (popularMoviesCache != null && popularMoviesCache!!.isNotEmpty()) {
            Log.w("DATABASE CALL VIEWMODEL", "getPopularMovies() Cache Returned!")
            Result.success(popularMoviesCache)
        } else {
            val response = apiHandler.getPopularMovies(timeWindow)
            Log.w("API CALL VIEWMODEL", "getPopularMovies() Called!")
            if (response != null && response.total_results > 0) {
                popularMoviesCache = response.results
                Result.success(response.results)
            } else {
                Result.failure(Exception("No result found"))
            }
        }
    }

    suspend fun getMoviesInTheatre(): Result<List<Result2>?> {
        return if (moviesInTheatreCache != null && moviesInTheatreCache!!.isNotEmpty()) {
            Log.w("DATABASE CALL VIEWMODEL", "getMoviesInTheatre() Cache Returned!")
            Result.success(moviesInTheatreCache)
        } else {
            val response = apiHandler.getNowPlayingMovies()
            Log.w("API CALL VIEWMODEL", "getMoviesInTheatre() Called!")
            if (response != null && response.total_results > 0) {
                moviesInTheatreCache = response.results
                Result.success(response.results)
            } else {
                Result.failure(Exception("No result found"))
            }
        }
    }

    suspend fun getUpcomingMovies(): Result<List<Result2>?> {
        return if (upcomingMoviesCache != null && upcomingMoviesCache!!.isNotEmpty()) {
            Log.w("DATABASE CALL VIEWMODEL", "getUpcomingMovies() Cache Returned!")
            Result.success(upcomingMoviesCache)
        } else {
            val response = apiHandler.getUpcomingMovies()
            Log.w("API CALL VIEWMODEL", "getUpcomingMovies() Called!")
            if (response != null && response.total_results > 0) {
                upcomingMoviesCache = response.results
                Result.success(response.results)
            } else {
                Result.failure(Exception("No result found"))
            }
        }
    }

    suspend fun getRecommendMovies(): Result<List<Recommend>> {
        return try {
            var response = recommendationEngine.getRecommendMovies()
            response = response.shuffled()
            response = response.take(15)
            Log.w("DATABASE CALL VIEWMODEL", "getRecommendMovies() Called!")
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}