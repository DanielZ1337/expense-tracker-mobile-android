package com.example.expense_tracker.models

import java.time.Instant
import java.time.LocalDateTime

data class Attachment(
    val id: Int,
    val name: String,
    val src: String,
    val createdAt: Instant,
    val updatedAt: Instant,
    val expenseAttachments: List<ExpenseAttachment>? = null
)

