package com.example.mediaapp.di

import com.example.mediaapp.backend.RecommendationEngine
import com.example.mediaapp.backend.auth.AuthRepository
import com.example.mediaapp.backend.auth.FirebaseAuthRepository
import com.example.mediaapp.backend.database.DatabaseRepository
import com.example.mediaapp.backend.database.FirebaseDatabaseRepository
import com.example.mediaapp.backend.repos.HomeRepo
import com.example.mediaapp.viewModels.LoginPageViewModel
import com.example.mediaapp.viewModels.MovieDetailViewModel
import com.example.mediaapp.viewModels.ProfileCustomizationViewModel
import com.example.mediaapp.viewModels.WatchlistViewModel
import com.example.mediaapp.viewModels.HomeViewModel
import com.example.mediaapp.rating.RatingHandler

// Import other ViewModels and Repositories as you add them

import org.koin.dsl.module
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import org.koin.core.module.dsl.viewModel

val appModule = module {
    // Single instance of AuthRepository (provided by FirebaseAuthRepository)
    single<AuthRepository> { FirebaseAuthRepository(databaseRepository = get()) }
    single<DatabaseRepository> { FirebaseDatabaseRepository(firestore = Firebase.firestore, authRepository = get()) }
    single<HomeRepo> { HomeRepo(recommendationEngine = get()) }
    // Define how to create LoginPageViewModel
    viewModel { LoginPageViewModel(authRepository = get()) } // 'get()' tells Koin to inject the AuthRepository dependency
    viewModel { MovieDetailViewModel(databaseRepository = get(), recommendationEngine = get(), ratingHandler = get()) }
    viewModel { ProfileCustomizationViewModel(authRepository = get(), databaseRepository = get()) }
    viewModel { WatchlistViewModel(databaseRepository = get()) }
    viewModel { HomeViewModel(homeRepo = get()) }

    factory { RatingHandler(databaseRepository = get(), recommendationEngine = get()) }
    factory { RecommendationEngine(databaseRepository = get()) }

// Add other ViewModels and Repositories here
    // e.g., viewModel { com.example.mediaapp.viewModels.HomeViewModel(get()) }
    // e.g., single<MovieRepository> { MovieRepositoryImpl(get()) }
}