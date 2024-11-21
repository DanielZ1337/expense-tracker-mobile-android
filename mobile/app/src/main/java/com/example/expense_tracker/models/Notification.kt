package com.example.expense_tracker.models

import java.time.Instant

data class Notification(
    val id: Int,
    val senderId: Int,
    val recipientId: Int,
    val expenseId: Int?,
    val message: String,
    val isRead: Boolean = false,
    val createdAt: Instant,
    val updatedAt: Instant,
    val sender: User? = null,
    val recipient: User? = null,
    val expense: Expense? = null
)

