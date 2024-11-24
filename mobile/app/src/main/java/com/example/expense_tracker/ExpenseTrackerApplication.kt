// ExpenseTrackerApplication.kt
package com.example.expense_tracker

import android.app.Application
import com.example.expense_tracker.network.ApiClient

class ExpenseTrackerApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        ApiClient.init(this)
    }
}
