package com.example.mediaapp.di

import com.example.mediaapp.backend.RecommendationEngine
import com.example.mediaapp.backend.apirequests.APIHandler
import com.example.mediaapp.backend.auth.AuthRepository
import com.example.mediaapp.backend.auth.FirebaseAuthRepository
import com.example.mediaapp.backend.database.DatabaseRepository
import com.example.mediaapp.backend.database.FirebaseDatabaseRepository
import com.example.mediaapp.backend.repos.HomeRepo
import com.example.mediaapp.backend.repos.MovieDetailRepo
import com.example.mediaapp.backend.repos.SearchRepository
import com.example.mediaapp.backend.sorting.SortingHandler
import com.example.mediaapp.viewModels.LoginPageViewModel
import com.example.mediaapp.viewModels.MovieDetailViewModel
import com.example.mediaapp.viewModels.ProfileCustomizationViewModel
import com.example.mediaapp.viewModels.WatchlistViewModel
import com.example.mediaapp.viewModels.HomeViewModel
import com.example.mediaapp.rating.RatingHandler
import com.example.mediaapp.viewModels.CurrentUserViewModel
import com.example.mediaapp.viewModels.SearchViewModel
import org.koin.dsl.module
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import org.koin.core.module.dsl.viewModel

// Dependency Injection Module
val appModule = module {

    single<AuthRepository> { FirebaseAuthRepository() }
    single<DatabaseRepository> { FirebaseDatabaseRepository(firestore = Firebase.firestore, authRepository = get()) }
    single { APIHandler() }
    factory { RecommendationEngine(databaseRepository = get(), apiHandler = get()) }
    factory { RatingHandler(databaseRepository = get(), recommendationEngine = get()) }
    single { HomeRepo(recommendationEngine = get()) }
    single { SearchRepository(apiHandler = get()) }
    single { MovieDetailRepo(apiHandler = get()) }
    single { SortingHandler() }

    viewModel { LoginPageViewModel(authRepository = get()) }
    viewModel { MovieDetailViewModel(databaseRepository = get(), recommendationEngine = get(), ratingHandler = get(), movieDetailRepo = get()) }
    viewModel { ProfileCustomizationViewModel(authRepository = get(), databaseRepository = get()) }
    viewModel { WatchlistViewModel(databaseRepository = get(), sortingHandler = get()) }
    viewModel { HomeViewModel(homeRepo = get()) }
    viewModel { CurrentUserViewModel(authRepository = get(), databaseRepository = get()) }
    viewModel { SearchViewModel(searchRepository = get()) }

}