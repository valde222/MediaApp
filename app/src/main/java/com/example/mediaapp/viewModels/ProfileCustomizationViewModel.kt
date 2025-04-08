package com.example.mediaapp.viewModels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mediaapp.backend.auth.AuthRepository
import com.example.mediaapp.backend.database.DatabaseHandler
import com.example.mediaapp.backend.database.DatabaseRepository
import kotlinx.coroutines.launch

class ProfileCustomizationViewModel(
    private val authRepository: AuthRepository,
    private val databaseRepository: DatabaseRepository
) : ViewModel() {

    private var username = mutableStateOf("")
    var name = mutableStateOf("")
    private var location = mutableStateOf("")
    var description = mutableStateOf("")

    fun updateAccountDetails() {
        val userMap = hashMapOf(
            "username" to username.value,
            "name" to name.value,
            "location" to location.value,
            "description" to description.value
        )
        viewModelScope.launch {
            val user = authRepository.getCurrentUser()

            user?.let { databaseRepository.updateUserProfile(it.uid, userMap) }
        }

    }
}