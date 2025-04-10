package com.example.mediaapp.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mediaapp.backend.RecommendationEngine
import com.example.mediaapp.models.RatingAverage
import com.example.mediaapp.backend.apirequests.APIHandler
import com.example.mediaapp.backend.database.DatabaseRepository
import com.example.mediaapp.models.TMDBMovieCredits
import com.example.mediaapp.models.TMDBMovieDetail
import com.example.mediaapp.rating.RatingHandler
import com.example.mediaapp.backend.repos.MovieDetailRepo
import com.example.mediaapp.models.TMDBMovie
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MovieDetailViewModel(
    private val databaseRepository: DatabaseRepository,
    private val recommendationEngine: RecommendationEngine,
    private val ratingHandler: RatingHandler,
    private val movieDetailRepo: MovieDetailRepo
) : ViewModel() {

    private val _movieDetails = MutableStateFlow<TMDBMovieDetail?>(null)
    val movieDetails: StateFlow<TMDBMovieDetail?> = _movieDetails.asStateFlow()

    private val _movieCredits = MutableStateFlow<TMDBMovieCredits?>(null)
    val movieCredits: StateFlow<TMDBMovieCredits?> = _movieCredits.asStateFlow()

    private val _movieRating = MutableStateFlow<RatingAverage?>(null)
    val movieRating: StateFlow<RatingAverage?> = _movieRating.asStateFlow()

    private var _movieUserRating = MutableStateFlow<Long?>(0)
    val movieUserRating: StateFlow<Long?> = _movieUserRating

    private val _watchedBool = MutableStateFlow<Boolean>(false)
    val watchedBool: StateFlow<Boolean> = _watchedBool.asStateFlow()

    private val _isInWatchlist = MutableStateFlow(false)
    val isInWatchlist: StateFlow<Boolean> = _isInWatchlist.asStateFlow()

    private val _similarMovies = MutableStateFlow<List<TMDBMovie>?>(emptyList())
    val similarMovies: StateFlow<List<TMDBMovie>?> = _similarMovies.asStateFlow()

    private val _isLoading = MutableStateFlow(false)

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun fetchMovieDetails(movieId: String) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val details = movieDetailRepo.getMovieDetails(movieId)
                _movieDetails.value = details
            } finally {
                _isLoading.value = false
            }
        }
    }
    fun fetchMovieCredits(movieId: String) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val credits = movieDetailRepo.getMovieCredits(movieId)
                _movieCredits.value = credits
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun fetchSimilarMovies(movieID: String) {
        viewModelScope.launch {
            try {
                val response = movieDetailRepo.getSimilarMovies(movieID)
                _similarMovies.value = response
            } catch (e: Exception) {
                Log.e("DATABASE", "fetchUserRating(): $e")
            }
        }
    }

    fun checkIfInWatchlist(movieId: String) {
        viewModelScope.launch {
            try {
                val watchlistMovies = this@MovieDetailViewModel.databaseRepository.getWatchlistMovies()
                val watchlistMovie = watchlistMovies.find { it.movieID == movieId.toLong() }
                _isInWatchlist.value = watchlistMovie != null
            } catch (e: Exception) {
                Log.e("DATABASE", "checkIfInWatchlist(): $e")
            }
        }
    }

    fun addToWatchlist(movieId: String) {
        viewModelScope.launch {
            try {
                if (recommendationEngine.containMovieId(movieId.toLong())) {
                    recommendationEngine.removeRecommendMovie(movieId.toLong())
                }
                this@MovieDetailViewModel.databaseRepository.addOrUpdateWatchlistMovie(createWatchlistMap())
                _isInWatchlist.value = true
            } catch (e: Exception) {
                Log.e("DATABASE", "addToWatchlist(): $e")
            }
        }
    }

    fun removeFromWatchlist(movieId: String) {
        viewModelScope.launch {
            try {
                this@MovieDetailViewModel.databaseRepository.removeWatchlistMovie(movieId.toLong())
                _isInWatchlist.value = false
            } catch (e: Exception) {
                Log.e("DATABASE", "removeFromWatchlist(): $e")
            }
        }
    }

    fun updateRating(movieId: String) {
        viewModelScope.launch {
            try {
                _movieRating.value = ratingHandler.getRating(movieId.toLong())
            } catch (e: Exception) {
                Log.e("DATABASE", "updateRating(): $e")
            }
        }
    }

    fun rateMovie(movieId: String, rating: Int) {
        viewModelScope.launch {
            try {
                ratingHandler.addRating(movieId.toLong(), rating)
                _movieRating.value = ratingHandler.getRating(movieId.toLong())
                fetchUserRating(movieId)
            } catch (e: Exception) {
                Log.e("DATABASE", "rateMovie(): $e")
            }
        }
    }

    //HERE HERE HERE
    fun fetchUserRating(movieID: String) {
        viewModelScope.launch {
            try {
                _movieUserRating.value = ratingHandler.getUserRating(movieID.toLong())
            } catch (e: Exception) {
                Log.e("DATABASE", "fetchUserRating(): $e")
            }
        }
    }


    fun checkIfWatched(movieId: String) {
        viewModelScope.launch {
            try {
                val watchedlistMovies = this@MovieDetailViewModel.databaseRepository.getWatchedMovies()
                val watchedlistMovie = watchedlistMovies.find { it.movieID == movieId.toLong() }
                _watchedBool.value = watchedlistMovie != null
            } catch (e: Exception) {
                Log.e("DATABASE", "checkIfWatched(): $e")
            }
        }
    }
    fun updateWatchedBool(movieId: String) {
        viewModelScope.launch {
            try {
                if (_watchedBool.value.not()) {
                    addToWatchedList(movieId)
                    _watchedBool.value = true
                    if (_isInWatchlist.value) {
                        this@MovieDetailViewModel.databaseRepository.addOrUpdateWatchlistMovie(createWatchlistMap())
                    }
                } else {
                    removeFromWatchedList(movieId)
                    _watchedBool.value = false
                    if (_isInWatchlist.value) {
                        this@MovieDetailViewModel.databaseRepository.addOrUpdateWatchlistMovie(createWatchlistMap())
                    }
                }
            } catch (e: Exception) {
                Log.e("DATABASE", "updateWatchedBool(): $e")
            }
        }
    }

    private fun addToWatchedList(movieId: String) {
        viewModelScope.launch {
            try {
                this@MovieDetailViewModel.databaseRepository.addWatchedMovie(createWatchlistMap())
            } catch (e: Exception) {
                Log.e("DATABASE", "addToWatchedList(): $e")
            }
        }
    }

    private fun removeFromWatchedList(movieId: String) {
        viewModelScope.launch {
            try {
                this@MovieDetailViewModel.databaseRepository.removeWatchedMovie(movieId.toLong())
            } catch (e: Exception) {
                Log.e("DATABASE", "removeFromWatchedList(): $e")
            }
        }
    }

    private fun createWatchlistMap() = hashMapOf(
        "movieID" to movieDetails.value?.id,
        "posterPath" to movieDetails.value?.poster_path,
        "title" to movieDetails.value?.title,
        "genres" to movieDetails.value?.genres,
        "description" to movieDetails.value?.overview,
        "release_date" to movieDetails.value?.release_date,
        "watched" to watchedBool.value
    )
}
