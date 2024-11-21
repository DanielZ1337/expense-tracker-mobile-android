package com.example.expense_tracker.network.repositories

import com.example.expense_tracker.models.User
import com.example.expense_tracker.network.ApiClient
import com.example.expense_tracker.network.responses.UserProfileResponse
import com.example.expense_tracker.network.services.interfaces.UserService

class UserRepository {

    private val userService = ApiClient.getRetrofit().create(UserService::class.java)

    suspend fun getUserProfile(): UserProfileResponse {
        return userService.getUserProfile()
    }

    suspend fun updateUserProfile(): User {
        return userService.updateUserProfile()
    }
}
