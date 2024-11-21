package com.example.expense_tracker.models

import java.time.LocalDateTime

data class Attachment(
    val id: Int,
    val name: String,
    val src: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val expenseAttachments: List<ExpenseAttachment>? = null
)

