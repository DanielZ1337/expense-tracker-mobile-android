package com.example.expense_tracker.models

import com.squareup.moshi.JsonClass
import java.time.LocalDateTime

@JsonClass(generateAdapter = true)
data class GroupExpense(
    val id: Int,
    val expenseId: Int,
    val groupId: Int,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val expense: Expense? = null,
    val group: Group? = null
)

