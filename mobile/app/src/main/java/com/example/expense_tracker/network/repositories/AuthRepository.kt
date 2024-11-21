package com.example.expense_tracker.network.repositories

import com.example.expense_tracker.models.TokenResponse
import com.example.expense_tracker.models.User
import com.example.expense_tracker.network.ApiClient
import com.example.expense_tracker.network.requests.LoginRequest
import com.example.expense_tracker.network.requests.RegisterRequest
import com.example.expense_tracker.network.services.interfaces.AuthService

class AuthRepository {

    private val authService = ApiClient.getRetrofit().create(AuthService::class.java)

    suspend fun register(request: RegisterRequest): User {
        return authService.register(request)
    }

    suspend fun login(request: LoginRequest): TokenResponse {
        val response = authService.login(request)
        ApiClient.tokenProvider.saveToken(response.token.token)
        return response
    }

    fun logout() {
        ApiClient.tokenProvider.clearToken()
    }
}
