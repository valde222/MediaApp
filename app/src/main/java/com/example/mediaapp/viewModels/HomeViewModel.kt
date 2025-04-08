package com.example.mediaapp.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mediaapp.models.Recommend
import com.example.mediaapp.models.Result2
import com.example.mediaapp.models.TMDBMovie
import com.example.mediaapp.backend.repos.HomeRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val homeRepo: HomeRepo
): ViewModel() {

    //private val apiHandler = APIHandler()
    //private val algorithm = RecommendationEngine()

    //States for pager
    private val _popularMovies = MutableStateFlow<List<TMDBMovie>>(emptyList())
    val popularMovies: StateFlow<List<TMDBMovie>> = _popularMovies.asStateFlow()

    //States for recommendation algorithm
    private val _recommendedMovies = MutableStateFlow<List<Recommend>>(emptyList())
    val recommendedMovies: StateFlow<List<Recommend>> = _recommendedMovies.asStateFlow()

    private val _recommendedState = MutableStateFlow<Boolean?>(null)
    val recommendedState: StateFlow<Boolean?> = _recommendedState

    //States for inTheatres Movies
    private val _inTheatres = MutableStateFlow<List<Result2>>(emptyList())
    val inTheatres: StateFlow<List<Result2>> = _inTheatres.asStateFlow()

    //States for upComingMovies
    private val _upComingMovies = MutableStateFlow<List<Result2>>(emptyList())
    val upComingMovies: StateFlow<List<Result2>> = _upComingMovies.asStateFlow()

    //Error States for Homepage
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun fetchPopularMovies() {
        viewModelScope.launch {
            homeRepo.getPopularMovies("week").also { result ->
                result.onSuccess { movies ->
                    if (movies != null) {
                        _popularMovies.value = movies
                    }
                }.onFailure { throwable ->
                    _error.value = throwable.localizedMessage ?: "Unknown Error"
                }
            }
        }
    }

    fun fetchRecommendedMovies() {
        viewModelScope.launch {
            homeRepo.getRecommendMovies().also { result ->
                result.onSuccess { movies ->
                    _recommendedMovies.value = movies
                }.onFailure { throwable ->
                    _error.value = throwable.localizedMessage ?: "Unknown Error"
                }
            }
        }
    }

    fun fetchMoviesInTheatre() {
        viewModelScope.launch {
            homeRepo.getMoviesInTheatre().also { result ->
                result.onSuccess { movies ->
                    if (movies != null) {
                        _inTheatres.value = movies
                    }
                }.onFailure { throwable ->
                    _error.value = throwable.localizedMessage ?: "Unknown Error"
                }
            }
        }
    }
    fun fetchUpcomingMovies() {
        viewModelScope.launch {
            homeRepo.getUpcomingMovies().also { result ->
                result.onSuccess { movies ->
                    if (movies != null) {
                        _upComingMovies.value = movies
                    }
                }.onFailure { throwable ->
                    _error.value = throwable.localizedMessage ?: "Unknown Error"
                }
            }
        }
    }
}
