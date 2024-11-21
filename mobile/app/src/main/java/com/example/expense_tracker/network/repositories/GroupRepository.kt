package com.example.expense_tracker.network.repositories

import com.example.expense_tracker.models.*
import com.example.expense_tracker.network.ApiClient
import com.example.expense_tracker.network.requests.*
import com.example.expense_tracker.network.services.interfaces.GroupService

class GroupRepository {

    private val groupService = ApiClient.getRetrofit().create(GroupService::class.java)

    suspend fun getGroups(): List<Group> {
        return groupService.getGroups()
    }

    suspend fun createGroup(request: CreateGroupRequest): Group {
        return groupService.createGroup(request)
    }

    suspend fun getGroup(groupId: Int): Group {
        return groupService.getGroup(groupId)
    }

    suspend fun deleteGroup(groupId: Int) {
        return groupService.deleteGroup(groupId)
    }

    suspend fun addMembers(groupId: Int, request: AddMembersRequest) {
        return groupService.addMembers(groupId, request)
    }

    suspend fun removeMembers(groupId: Int, request: RemoveMembersRequest) {
        return groupService.removeMembers(groupId, request)
    }

    suspend fun updateMembers(groupId: Int, request: UpdateMembersRequest) {
        return groupService.updateMembers(groupId, request)
    }

    suspend fun getGroupExpenses(groupId: Int): List<GroupExpense> {
        return groupService.getGroupExpenses(groupId)
    }

    suspend fun createGroupExpense(groupId: Int, request: CreateExpenseRequest): GroupExpense {
        return groupService.createGroupExpense(groupId, request)
    }

    suspend fun getGroupDebts(groupId: Int) {
        return groupService.getGroupDebts(groupId)
    }
}
