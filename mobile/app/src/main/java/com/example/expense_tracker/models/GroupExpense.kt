package com.example.expense_tracker.models

import java.time.LocalDateTime

data class GroupExpense(
    val id: Int,
    val expenseId: Int,
    val groupId: Int,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val expense: Expense? = null,
    val group: Group? = null
)

