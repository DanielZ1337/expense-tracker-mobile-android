package com.example.expense_tracker.models

import java.time.Instant

data class Group(
    val id: Int,
    val name: String,
    val description: String?,
    val createdAt: Instant,
    val updatedAt: Instant,
    val members: List<GroupMember>? = null,
    val expenses: List<GroupExpense>? = null
)
