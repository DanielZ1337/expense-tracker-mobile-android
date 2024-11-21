package com.example.expense_tracker.models

import com.squareup.moshi.JsonClass
import java.time.LocalDateTime

@JsonClass(generateAdapter = true)
data class ExpenseAttachment(
    val id: Int,
    val expenseId: Int,
    val attachmentId: Int,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val expense: Expense? = null,
    val attachment: Attachment? = null
)

