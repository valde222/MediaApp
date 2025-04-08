package com.example.mediaapp.rating

import com.example.mediaapp.backend.RecommendationEngine
import com.example.mediaapp.backend.database.DatabaseHandler
import com.example.mediaapp.backend.database.DatabaseRepository
import com.example.mediaapp.models.RatingAverage
import com.example.mediaapp.models.RatingForDatabase

class RatingHandler(
    private val databaseRepository: DatabaseRepository,
    private val recommendationEngine: RecommendationEngine
) {
    suspend fun getRating(movieID: Long): RatingAverage {
        val rating = this.databaseRepository.getMovieRatings(movieID.toString())
        val amount = rating.size
        val average = rating.map { it.rating }.average()

        return RatingAverage(movieID, average, amount)
    }

    suspend fun addRating(movieID: Long, rating: Int) {
        val ratingForDatabase = RatingForDatabase(movieID, rating.toLong())
        this.databaseRepository.addOrUpdateUserRating(movieID.toString(), ratingForDatabase)
        this.databaseRepository.addOrUpdateMovieRating(movieID.toString(), ratingForDatabase)
        if (rating >= 7) {
            recommendationEngine.generateMovieSuggestions(movieID.toString())
        }
    }

    suspend fun getUserRating(movieID: Long) : Long? {
        val response = this.databaseRepository.getUserRatings()
        response.forEach{item ->
            if(item.movieID == movieID) {
                return item.rating
            }
        }
        return null
    }
}