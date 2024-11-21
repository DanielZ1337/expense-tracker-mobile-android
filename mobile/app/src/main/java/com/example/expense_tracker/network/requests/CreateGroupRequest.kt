package com.example.expense_tracker.network.requests

data class CreateGroupRequest(
    val name: String,
    val description: String? = null,
    val participantEmails: List<String>? = null
)
