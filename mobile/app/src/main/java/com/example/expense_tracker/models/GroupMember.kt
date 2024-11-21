package com.example.expense_tracker.models

import java.time.LocalDateTime

data class GroupMember(
    val id: Int,
    val groupId: Int,
    val userId: Int,
    val receiveNotifications: Boolean = true,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val user: User? = null,
    val group: Group? = null
)
