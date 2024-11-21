package com.example.expense_tracker.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TokenResponse(
    val message: String,
    val token: Token,
    val decoded: DecodedToken
)

@JsonClass(generateAdapter = true)
data class DecodedToken(
    val email: String,
    val name: String,
    val userId: Int,
    val iat: Long,
    val exp: Long
)
