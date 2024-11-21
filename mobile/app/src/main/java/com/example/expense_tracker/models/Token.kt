package com.example.expense_tracker.models

import java.time.Instant

data class Token(
    val id: Int,
    val userId: Int,
    val token: String,
    val createdAt: Instant,
    val updatedAt: Instant
)

