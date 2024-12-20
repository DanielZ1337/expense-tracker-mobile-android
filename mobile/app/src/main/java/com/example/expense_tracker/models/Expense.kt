package com.example.expense_tracker.models

import java.time.Instant

data class Expense(
    val id: Int,
    val title: String,
    val payerId: Int,
    val amount: Int, // Amount in smallest currency unit (e.g., cents)
    val date: Instant,
    val createdAt: Instant,
    val updatedAt: Instant,
    val attachments: List<ExpenseAttachment>? = null,
    val participants: List<ExpenseParticipant>? = null,
    val groupExpenses: List<GroupExpense>? = null,
    val notifications: List<Notification>? = null,
    val payer: User? = null
)

