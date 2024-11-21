package com.example.expense_tracker.models

import java.time.Instant

data class GroupExpense(
    val id: Int,
    val expenseId: Int,
    val groupId: Int,
    val createdAt: Instant,
    val updatedAt: Instant,
    val expense: Expense? = null,
    val group: Group? = null
)

