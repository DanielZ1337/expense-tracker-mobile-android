package com.example.expense_tracker.network.requests

data class CreateExpenseRequest(
    val amount: Int,
    val date: String? = null, // Use ISO 8601 format, e.g., "2023-10-05T14:48:00.000Z"
    val payerEmail: String,
    val title: String,
    val participantEmails: List<String>? = null
)
