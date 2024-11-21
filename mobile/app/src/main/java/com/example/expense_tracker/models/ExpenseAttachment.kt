package com.example.expense_tracker.models

import java.time.Instant

data class ExpenseAttachment(
    val id: Int,
    val expenseId: Int,
    val attachmentId: Int,
    val createdAt: Instant,
    val updatedAt: Instant,
    val expense: Expense? = null,
    val attachment: Attachment? = null
)

