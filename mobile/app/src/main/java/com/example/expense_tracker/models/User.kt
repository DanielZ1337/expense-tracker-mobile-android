package com.example.expense_tracker.models

import java.time.Instant

data class User(
    val id: Int,
    val email: String,
    val name: String,
    val phoneNo: String?,
    val createdAt: Instant,
    val updatedAt: Instant,
    val tokens: List<Token>? = null,
    val groupMembers: List<GroupMember>? = null,
    val expenses: List<Expense>? = null,
    val expenseParticipants: List<ExpenseParticipant>? = null,
    val sentNotifications: List<Notification>? = null,
    val receivedNotifications: List<Notification>? = null
)
