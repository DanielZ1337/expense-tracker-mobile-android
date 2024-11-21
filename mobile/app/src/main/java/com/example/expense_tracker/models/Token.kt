package com.example.expense_tracker.models

import com.squareup.moshi.JsonClass
import java.time.LocalDateTime

@JsonClass(generateAdapter = true)
data class Token(
    val id: Int,
    val userId: Int,
    val token: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)

