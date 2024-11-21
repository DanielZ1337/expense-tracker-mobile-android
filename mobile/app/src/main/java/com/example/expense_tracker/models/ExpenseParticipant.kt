package com.example.expense_tracker.models

import java.time.LocalDateTime

data class ExpenseParticipant(
    val id: Int,
    val expenseId: Int,
    val userId: Int,
    val paidAmount: Int = 0,
    val owedAmount: Int = 0,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val user: User? = null,
    val expense: Expense? = null
)

