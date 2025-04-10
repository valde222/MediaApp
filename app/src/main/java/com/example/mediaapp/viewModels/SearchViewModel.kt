package com.example.mediaapp.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mediaapp.backend.apirequests.APIHandler
import com.example.mediaapp.backend.repos.SearchRepository
import com.example.mediaapp.models.TMDBMovie
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SearchViewModel(
    private val searchRepository: SearchRepository
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String?> = _searchQuery

    private val _searchResults = MutableStateFlow<List<TMDBMovie>>(emptyList())
    val searchResults: StateFlow<List<TMDBMovie>> = _searchResults

    private val _isSearchActive = MutableStateFlow(false)
    val isSearchActive: StateFlow<Boolean> = _isSearchActive

    private val _hasGenreBeenChosen = MutableStateFlow(false)
    val hasGenreBeenChosen: StateFlow<Boolean> = _hasGenreBeenChosen

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error


    fun setSearchQuery(query: String) {
        _searchQuery.value = query // Method to update search query
    }

    fun performSearch(query: String) {
        if (query.isNotEmpty()) {
            _isSearchActive.value = true
            viewModelScope.launch {
                searchRepository.searchMovies(query).also { result ->
                    result.onSuccess { movies ->
                        _searchResults.value = movies
                        _isSearchActive.value = true
                    }.onFailure { throwable ->
                        _error.value = throwable.localizedMessage ?: "Unknown Error"
                    }
                }
            }
        }
        else {
            _isSearchActive.value = false
            resetSearch()
        }
    }

    fun getMoviesWithGenre(genre: String) {
        viewModelScope.launch {
            searchRepository.searchMoviesWithGenre(genre).also { result ->
                result.onSuccess { movies ->
                    _searchResults.value = movies
                    _hasGenreBeenChosen.value = true
                }.onFailure { throwable ->
                    _error.value = throwable.localizedMessage ?: "Error with getting movies by genre"
                }
            }
        }
    }


    private fun resetSearch() {
        _searchQuery.value = ""
        _searchResults.value = emptyList()
        _error.value = null
    }
}

