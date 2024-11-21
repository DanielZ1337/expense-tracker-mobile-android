package com.example.expense_tracker.network.repositories

import com.example.expense_tracker.models.*
import com.example.expense_tracker.network.ApiClient
import com.example.expense_tracker.network.requests.PayExpenseRequest
import com.example.expense_tracker.network.services.interfaces.ExpenseService

class ExpenseRepository {

    private val expenseService = ApiClient.getRetrofit().create(ExpenseService::class.java)

    suspend fun payExpense(groupId: Int, expenseId: Int, request: PayExpenseRequest) {
        return expenseService.payExpense(groupId, expenseId, request)
    }

    suspend fun sendReminder(groupId: Int, expenseId: Int) {
        return expenseService.sendReminder(groupId, expenseId)
    }

    suspend fun getExpense(groupId: Int, expenseId: Int): GroupExpense {
        return expenseService.getExpense(groupId, expenseId)
    }

    suspend fun deleteExpense(groupId: Int, expenseId: Int) {
        return expenseService.deleteExpense(groupId, expenseId)
    }
}
