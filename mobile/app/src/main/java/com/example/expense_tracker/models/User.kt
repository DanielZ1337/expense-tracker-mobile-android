package com.example.expense_tracker.models

import com.squareup.moshi.JsonClass
import java.time.LocalDateTime

@JsonClass(generateAdapter = true)
data class User(
    val id: Int,
    val email: String,
    val name: String,
    val phoneNo: String?,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val tokens: List<Token>? = null,
    val groupMembers: List<GroupMember>? = null,
    val expenses: List<Expense>? = null,
    val expenseParticipants: List<ExpenseParticipant>? = null,
    val sentNotifications: List<Notification>? = null,
    val receivedNotifications: List<Notification>? = null
)