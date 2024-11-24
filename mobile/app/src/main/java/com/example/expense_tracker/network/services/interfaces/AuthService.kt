package com.example.expense_tracker.network.services.interfaces

import com.example.expense_tracker.models.TokenResponse
import com.example.expense_tracker.models.User
import com.example.expense_tracker.network.requests.LoginRequest
import com.example.expense_tracker.network.requests.RegisterRequest
import com.example.expense_tracker.network.responses.SessionResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthService {

    @POST("/api/auth/register")
    suspend fun register(@Body request: RegisterRequest): User

    @POST("/api/auth/login")
    suspend fun login(@Body request: LoginRequest): TokenResponse

    @GET("/api/auth/session")
    suspend fun getSession(): SessionResponse?
}
