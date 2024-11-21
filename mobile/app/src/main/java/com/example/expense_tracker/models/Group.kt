package com.example.expense_tracker.models

import java.time.LocalDateTime

data class Group(
    val id: Int,
    val name: String,
    val description: String?,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val members: List<GroupMember>? = null,
    val expenses: List<GroupExpense>? = null
)
