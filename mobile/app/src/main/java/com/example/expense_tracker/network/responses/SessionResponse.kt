package com.example.expense_tracker.network.responses

data class SessionResponse(
    val user: SessionUser
)

data class SessionUser(
    val id: Int,
    val email: String,
    val name: String,
)
