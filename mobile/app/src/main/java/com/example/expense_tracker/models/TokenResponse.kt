package com.example.expense_tracker.models

import java.io.Serializable

data class TokenResponse(
    val message: String,
    val token: Token,
    val decoded: DecodedToken
) : Serializable

data class DecodedToken(
    val email: String,
    val name: String,
    val userId: Int,
    val iat: Long,
    val exp: Long
)
