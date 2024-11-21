package com.example.expense_tracker.models

import com.squareup.moshi.JsonClass
import java.time.LocalDateTime

@JsonClass(generateAdapter = true)
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

