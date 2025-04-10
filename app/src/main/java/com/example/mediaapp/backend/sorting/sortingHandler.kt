package com.example.mediaapp.backend.sorting

import com.example.mediaapp.models.WatchlistMovie

class SortingHandler {
    fun sortWatchListMoviesAlphabetically(movies: List<WatchlistMovie>, ascending: Boolean = true): List<WatchlistMovie> {
        return if (ascending) {
            movies.sortedBy { it.title }
        } else {
            movies.sortedByDescending { it.title }
        }
    }
    fun sortWatchListMoviesByYear(movies: List<WatchlistMovie>, ascending: Boolean): List<WatchlistMovie> {
        return if (ascending) {
            movies.sortedByDescending { it.release_date }
        } else {
            movies.sortedBy { it.release_date }
        }
    }
    fun filterWatchListMoviesByGenre(movies: List<WatchlistMovie>, genre: String): List<WatchlistMovie> {
        return movies.filter { movie ->
            val genreNames = extractGenreNames(movie)
            genre in genreNames
        }
    }
    private fun extractGenreNames(watchlistMovie: WatchlistMovie): List<String> {
        val genreList = mutableListOf<String>()
        for (i in 0 until watchlistMovie.genres.size) {
            val tempGenre = watchlistMovie.genres[i] as? HashMap<*, *>
            val genreName = tempGenre?.get("name") as? String
            genreList.add(genreName ?: "")
        }
        return genreList
    }

    fun filterWatchedMovies(watchlistMovieList: List<WatchlistMovie>, isItWatched: Boolean): List<WatchlistMovie> {
        return watchlistMovieList.filter { watchlistMovie ->
            watchlistMovie.watched == isItWatched
        }
    }
}