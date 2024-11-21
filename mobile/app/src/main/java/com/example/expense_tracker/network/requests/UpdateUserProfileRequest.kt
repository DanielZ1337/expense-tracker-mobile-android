package com.example.expense_tracker.network.requests

data class UpdateUserProfileRequest(
    val name: String? = null,
    val email: String? = null,
    val phoneNo: String? = null
    // Add other fields if updatable
)
