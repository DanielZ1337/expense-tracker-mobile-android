package com.example.expense_tracker.network.responses

import java.io.Serializable

data class UserProfileResponse(
    val id: Int,
    val name: String,
    val email: String
): Serializable
