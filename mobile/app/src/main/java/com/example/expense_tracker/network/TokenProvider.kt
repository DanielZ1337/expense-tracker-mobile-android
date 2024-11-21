package com.example.expense_tracker.network

interface TokenProvider {
    fun getToken(): String?
    fun saveToken(token: String)
    fun clearToken()
}