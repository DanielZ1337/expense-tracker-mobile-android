package com.example.expense_tracker.network.repositories

import android.util.Log
import com.example.expense_tracker.models.TokenResponse
import com.example.expense_tracker.models.User
import com.example.expense_tracker.network.ApiClient
import com.example.expense_tracker.network.services.interfaces.AuthService
import com.example.expense_tracker.network.requests.LoginRequest
import com.example.expense_tracker.network.requests.RegisterRequest
import com.example.expense_tracker.network.responses.SessionResponse
import retrofit2.HttpException
import java.io.IOException

class AuthRepository private constructor() {

    private val authService: AuthService = ApiClient.getRetrofit().create(AuthService::class.java)
    private val tokenProvider = ApiClient.tokenProvider

    companion object {
        @Volatile
        private var INSTANCE: AuthRepository? = null

        fun getInstance(): AuthRepository {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: AuthRepository().also { INSTANCE = it }
            }
        }
    }

    suspend fun register(request: RegisterRequest): User {
        return authService.register(request)
    }

    suspend fun login(request: LoginRequest): TokenResponse {
        val response = authService.login(request)
        tokenProvider.saveToken(response.token.token) // Save the token upon successful login
        return response
    }

    suspend fun getSession(): SessionResponse? {
        val token = tokenProvider.getToken()
        Log.d("AuthRepository", "Getting token $token")
        return if (token != null) {
            try {
                authService.getSession()
            } catch (e: HttpException) {
                if (e.code() == 401) {
                    // Token is invalid or expired
                    logout() // Clear the invalid token
                    null
                } else {
                    throw e
                }
            } catch (e: IOException) {
                throw e
            }
        } else {
            null
        }
    }

    fun logout() {
        tokenProvider.clearToken()
    }
}
