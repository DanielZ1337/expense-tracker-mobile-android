package com.example.expense_tracker.models

import java.time.Instant

data class ExpenseParticipant(
    val id: Int,
    val expenseId: Int,
    val userId: Int,
    val paidAmount: Int = 0,
    val owedAmount: Int = 0,
    val createdAt: Instant,
    val updatedAt: Instant,
    val user: User? = null,
    val expense: Expense? = null
)

