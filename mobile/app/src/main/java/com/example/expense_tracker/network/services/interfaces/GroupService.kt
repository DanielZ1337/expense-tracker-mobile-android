package com.example.expense_tracker.network.services.interfaces

import com.example.expense_tracker.models.*
import com.example.expense_tracker.network.requests.*
import retrofit2.http.*

interface GroupService {

    @GET("groups")
    suspend fun getGroups(): List<Group>

    @POST("groups")
    suspend fun createGroup(@Body request: CreateGroupRequest): Group

    @GET("groups/{groupId}")
    suspend fun getGroup(@Path("groupId") groupId: Int): Group

    @DELETE("groups/{groupId}")
    suspend fun deleteGroup(@Path("groupId") groupId: Int)

    @POST("groups/{groupId}/members")
    suspend fun addMembers(
        @Path("groupId") groupId: Int,
        @Body request: AddMembersRequest
    )

    @DELETE("groups/{groupId}/members")
    suspend fun removeMembers(
        @Path("groupId") groupId: Int,
        @Body request: RemoveMembersRequest
    )

    @PUT("groups/{groupId}/members")
    suspend fun updateMembers(
        @Path("groupId") groupId: Int,
        @Body request: UpdateMembersRequest
    )

    @GET("groups/{groupId}/debts")
    suspend fun getGroupDebts(@Path("groupId") groupId: Int)

    @GET("groups/{groupId}/expenses")
    suspend fun getGroupExpenses(@Path("groupId") groupId: Int): List<GroupExpense>

    @POST("groups/{groupId}/expenses")
    suspend fun createGroupExpense(
        @Path("groupId") groupId: Int,
        @Body request: CreateExpenseRequest
    ): GroupExpense
}
