package com.example.expense_tracker.models

import java.time.LocalDateTime

data class Token(
    val id: Int,
    val userId: Int,
    val token: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)

