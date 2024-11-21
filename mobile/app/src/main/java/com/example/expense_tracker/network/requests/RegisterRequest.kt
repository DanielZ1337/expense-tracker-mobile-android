package com.example.expense_tracker.network.requests

data class RegisterRequest(
    val email: String,
    val password: String,
    val name: String,
    val passwordConfirmation: String
)
