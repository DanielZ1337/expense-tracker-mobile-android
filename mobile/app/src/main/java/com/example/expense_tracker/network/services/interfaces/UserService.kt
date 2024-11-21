package com.example.expense_tracker.network.services.interfaces

import com.example.expense_tracker.models.User
import com.example.expense_tracker.network.responses.UserProfileResponse
import retrofit2.http.GET
import retrofit2.http.PUT

interface UserService {

    @GET("/api/users/profile")
    suspend fun getUserProfile(): UserProfileResponse

    @PUT("/api/users/profile")
    suspend fun updateUserProfile(): User

}
