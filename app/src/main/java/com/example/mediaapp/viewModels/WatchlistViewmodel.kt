package com.example.mediaapp.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mediaapp.backend.database.DatabaseHandler
import com.example.mediaapp.backend.database.DatabaseRepository
import com.example.mediaapp.backend.sorting.SortingHandler
import com.example.mediaapp.models.WatchlistMovie
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class WatchlistViewModel(
    private val databaseRepository: DatabaseRepository,
    private val sortingHandler: SortingHandler
) : ViewModel() {

    private val _originalWatchlist = MutableStateFlow<List<WatchlistMovie>?>(null)

    private val _filteredWatchList = MutableStateFlow<List<WatchlistMovie>?>(null)
    val filteredWatchList: StateFlow<List<WatchlistMovie>?> = _filteredWatchList.asStateFlow()

    private val _deleteview = MutableStateFlow(false)
    val deleteview: StateFlow<Boolean> = _deleteview.asStateFlow()

    fun getWatchlistMovies() {
        viewModelScope.launch {
            val watchlist = this@WatchlistViewModel.databaseRepository.getWatchlistMovies()
            _originalWatchlist.value = watchlist
            _filteredWatchList.value = watchlist
            Log.w("DATABASE CALL", "getWatchlistMovies() Called!")
        }
    }
    fun openDeleteView() {
        _deleteview.value = !_deleteview.value
    }

    fun removeMovieFromWatchlist(movieID: Long) {
        viewModelScope.launch {
            this@WatchlistViewModel.databaseRepository.removeWatchlistMovie(movieID)
            val watchlist = this@WatchlistViewModel.databaseRepository.getWatchlistMovies()
            _originalWatchlist.value = watchlist
            _filteredWatchList.value = watchlist
        }
    }
    fun onFilterOptionSelected(filterId: String, option: String) {
        when (filterId) {
            "Genre" -> filterMoviesByGenre(option)
            "Year" -> filterMoviesByDate(option)
            "Name" -> filterMoviesByName(option)
        }
    }
    private fun filterMoviesByGenre(genre: String) {
        viewModelScope.launch {
            val filteredMovies = sortingHandler.filterWatchListMoviesByGenre(
                _originalWatchlist.value ?: emptyList(),
                genre
            )
            _filteredWatchList.value = filteredMovies
        }
    }
    private fun filterMoviesByDate(order: String) {
        viewModelScope.launch {
            val ascending = order == "Year Asc"
            val filteredMovies = sortingHandler.sortWatchListMoviesByYear(
                _filteredWatchList.value ?: emptyList(), ascending
            )
            _filteredWatchList.value = filteredMovies
        }
    }
    private fun filterMoviesByName(order: String) {
        viewModelScope.launch {
            val ascending = order == "Name Asc"
            val filteredMovies = sortingHandler.sortWatchListMoviesAlphabetically(
                _filteredWatchList.value ?: emptyList(), ascending
            )
            _filteredWatchList.value = filteredMovies
        }
    }
    fun filterWatchedMovies(watched: String) {
        viewModelScope.launch {
            val isItWatched = watched == "Watched"
            val filteredMovies = if (watched == "All") {
                _originalWatchlist.value ?: emptyList()
            } else {
                sortingHandler.filterWatchedMovies(_originalWatchlist.value ?: emptyList(), isItWatched)
            }
            _filteredWatchList.value = filteredMovies
        }
    }
}