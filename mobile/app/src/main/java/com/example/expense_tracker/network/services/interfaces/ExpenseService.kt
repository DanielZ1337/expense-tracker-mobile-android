package com.example.expense_tracker.network.services.interfaces

import com.example.expense_tracker.models.*
import com.example.expense_tracker.network.requests.PayExpenseRequest
import retrofit2.http.*

interface ExpenseService {

    @PUT("groups/{groupId}/expenses/{expenseId}/pay")
    suspend fun payExpense(
        @Path("groupId") groupId: Int,
        @Path("expenseId") expenseId: Int,
        @Body request: PayExpenseRequest
    )

    @POST("groups/{groupId}/expenses/{expenseId}/reminder")
    suspend fun sendReminder(
        @Path("groupId") groupId: Int,
        @Path("expenseId") expenseId: Int
    )

    @GET("groups/{groupId}/expenses/{expenseId}")
    suspend fun getExpense(
        @Path("groupId") groupId: Int,
        @Path("expenseId") expenseId: Int
    ): GroupExpense

    @DELETE("groups/{groupId}/expenses/{expenseId}")
    suspend fun deleteExpense(
        @Path("groupId") groupId: Int,
        @Path("expenseId") expenseId: Int
    )
}
