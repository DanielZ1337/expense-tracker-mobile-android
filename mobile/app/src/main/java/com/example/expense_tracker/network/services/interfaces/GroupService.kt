package com.example.expense_tracker.network.services.interfaces

import com.example.expense_tracker.models.*
import com.example.expense_tracker.network.requests.*
import retrofit2.http.*

interface GroupService {

    @GET("/api/groups")
    suspend fun getGroups(): List<Group>

    @POST("/api/groups")
    suspend fun createGroup(@Body request: CreateGroupRequest): Group

    @GET("/api/groups/{groupId}")
    suspend fun getGroup(@Path("groupId") groupId: Int): Group

    @DELETE("/api/groups/{groupId}")
    suspend fun deleteGroup(@Path("groupId") groupId: Int)

    @POST("/api/groups/{groupId}/members")
    suspend fun addMembers(
        @Path("groupId") groupId: Int,
        @Body request: AddMembersRequest
    )

    @DELETE("/api/groups/{groupId}/members")
    suspend fun removeMembers(
        @Path("groupId") groupId: Int,
        @Body request: RemoveMembersRequest
    )

    @PUT("/api/groups/{groupId}/members")
    suspend fun updateMembers(
        @Path("groupId") groupId: Int,
        @Body request: UpdateMembersRequest
    )

    @GET("/api/groups/{groupId}/debts")
    suspend fun getGroupDebts(@Path("groupId") groupId: Int)

    @GET("/api/groups/{groupId}/expenses")
    suspend fun getGroupExpenses(@Path("groupId") groupId: Int): List<GroupExpense>

    @POST("/api/groups/{groupId}/expenses")
    suspend fun createGroupExpense(
        @Path("groupId") groupId: Int,
        @Body request: CreateExpenseRequest
    ): GroupExpense
}
