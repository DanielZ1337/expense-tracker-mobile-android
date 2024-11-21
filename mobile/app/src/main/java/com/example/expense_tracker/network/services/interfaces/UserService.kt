package com.example.expense_tracker.network.services.interfaces

import com.example.expense_tracker.models.User
import retrofit2.http.GET
import retrofit2.http.PUT

interface UserService {

    @GET("/api/user/profile")
    suspend fun getUserProfile(): User

    @PUT("/api/user/profile")
    suspend fun updateUserProfile(): User

}
