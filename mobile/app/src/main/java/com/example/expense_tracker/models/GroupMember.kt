package com.example.expense_tracker.models

import java.time.Instant

data class GroupMember(
    val id: Int,
    val groupId: Int,
    val userId: Int,
    val receiveNotifications: Boolean = true,
    val createdAt: Instant,
    val updatedAt: Instant,
    val user: User? = null,
    val group: Group? = null
)
