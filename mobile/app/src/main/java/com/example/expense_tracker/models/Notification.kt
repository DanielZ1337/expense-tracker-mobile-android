package com.example.expense_tracker.models

import java.time.LocalDateTime

data class Notification(
    val id: Int,
    val senderId: Int,
    val recipientId: Int,
    val expenseId: Int?,
    val message: String,
    val isRead: Boolean = false,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val sender: User? = null,
    val recipient: User? = null,
    val expense: Expense? = null
)

