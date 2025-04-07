package com.example.mediaapp.di

import com.example.mediaapp.backend.auth.AuthRepository
import com.example.mediaapp.backend.auth.FirebaseAuthRepository
import com.example.mediaapp.viewModels.LoginPageViewModel
// Import other ViewModels and Repositories as you add them
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    // Single instance of AuthRepository (provided by FirebaseAuthRepository)
    single<AuthRepository> { FirebaseAuthRepository() } // Koin will provide FirebaseAuth and DatabaseHandler if they are also defined in modules, otherwise manually create here

    // Define how to create LoginPageViewModel
    viewModel { LoginPageViewModel(get()) } // 'get()' tells Koin to inject the AuthRepository dependency

    // Add other ViewModels and Repositories here
    // e.g., viewModel { HomeViewModel(get()) }
    // e.g., single<MovieRepository> { MovieRepositoryImpl(get()) }
}