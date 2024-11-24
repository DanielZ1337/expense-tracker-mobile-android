package com.example.expense_tracker

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.expense_tracker.databinding.ActivityGroupViewBinding
import com.example.expense_tracker.models.Group
import com.example.expense_tracker.models.GroupExpense
import com.example.expense_tracker.network.repositories.AuthRepository
import com.example.expense_tracker.network.repositories.ExpenseRepository
import com.example.expense_tracker.network.repositories.GroupRepository
import com.example.expense_tracker.network.responses.SessionResponse
import com.example.expense_tracker.ui.adapters.GroupExpenseLogRecyclerAdapter
import com.example.expense_tracker.ui.adapters.GroupMemberRecyclerAdapter
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import java.io.Serializable

class GroupViewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGroupViewBinding
    private lateinit var authRepository: AuthRepository
    private lateinit var groupRepository: GroupRepository
    private lateinit var expenseRepository: ExpenseRepository

    private var groupId: Int? = null
    private var group: Group? = null
    private var userProfileResponse: SessionResponse? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityGroupViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        authRepository = AuthRepository.getInstance()
        groupRepository = GroupRepository()
        expenseRepository = ExpenseRepository()

        groupId = intent.getIntExtra("groupId", 0)
        if (groupId == null) {
            Log.e("GroupViewActivity", "groupId is missing in Intent")
            Toast.makeText(this, "Group ID is missing.", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        binding.groupNameTextView.text = "Loading..."
        binding.groupDescriptionTextView.text = "Please wait..."

        fetchUserSessionAndGroupDetails()

        binding.groupGoBackButton.setOnClickListener {
            finish()
        }

        binding.expensesButton.setOnClickListener {
            if (userProfileResponse != null) {
                val intent = Intent(this, ExpenseInputActivity::class.java).apply {
                    putExtra("userProfile", userProfileResponse as Serializable)
                    putExtra("groupId", groupId)
                }
                startActivity(intent)
            } else {
                Toast.makeText(this, "User session not found.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun fetchUserSessionAndGroupDetails() {
        Log.d("GroupViewActivity", "Starting to fetch user session and group details.")
        lifecycleScope.launch {
            try {
                userProfileResponse = authRepository.getSession()
                if (userProfileResponse == null) {
                    Log.e("GroupViewActivity", "User profile not found. Please log in again.")
                    Toast.makeText(this@GroupViewActivity, "User not authenticated. Please log in again.", Toast.LENGTH_SHORT).show()
                    navigateToLogin()
                    return@launch
                }
                Log.d("GroupViewActivity", "User session fetched successfully.")

                fetchGroupDetails()
            } catch (e: Exception) {
                Log.e("GroupViewActivity", "Error while fetching user session", e)
                Toast.makeText(this@GroupViewActivity, "An error occurred. Please try again.", Toast.LENGTH_SHORT).show()
                navigateToLogin()
            }
        }
    }

    private suspend fun fetchGroupDetails() {
        Log.d("GroupViewActivity", "Fetching group details for groupId: $groupId")
        try {
            group = groupRepository.getGroup(groupId!!)
            if (group == null) {
                Log.e("GroupViewActivity", "No group found with groupId: $groupId")
                Toast.makeText(this@GroupViewActivity, "Group not found.", Toast.LENGTH_SHORT).show()
                finish()
                return
            }

            binding.groupNameTextView.text = group!!.name
            binding.groupDescriptionTextView.text = group!!.description ?: "No description"

            fetchGroupExpenses()

            displayGroupMembers()

            Log.d("GroupViewActivity", "Group details fetched successfully.")

        } catch (e: IOException) {
            Log.e("GroupViewActivity", "Network error while fetching group details", e)
            Toast.makeText(this@GroupViewActivity, "Failed to load group details. Please check your connection.", Toast.LENGTH_SHORT).show()
        } catch (e: HttpException) {
            Log.e("GroupViewActivity", "HTTP error while fetching group details", e)
            Toast.makeText(this@GroupViewActivity, "Failed to load group details. Please try again later.", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Log.e("GroupViewActivity", "Unexpected error while fetching group details", e)
            Toast.makeText(this@GroupViewActivity, "An unexpected error occurred.", Toast.LENGTH_SHORT).show()
        }
    }

    private suspend fun fetchGroupExpenses() {
        Log.d("GroupViewActivity", "Fetching expenses for groupId: $groupId")
        try {
            val expenses: List<GroupExpense> = groupRepository.getGroupExpenses(groupId!!)
            val expenseAdapter = GroupExpenseLogRecyclerAdapter(expenses)
            binding.groupViewExpenseLogRecycler.apply {
                adapter = expenseAdapter
                layoutManager = LinearLayoutManager(this@GroupViewActivity)
            }
            Log.d("GroupViewActivity", "Expenses fetched and displayed successfully.")
        } catch (e: IOException) {
            Log.e("GroupViewActivity", "Network error while fetching expenses", e)
            Toast.makeText(this@GroupViewActivity, "Failed to load expenses. Please check your connection.", Toast.LENGTH_SHORT).show()
        } catch (e: HttpException) {
            Log.e("GroupViewActivity", "HTTP error while fetching expenses", e)
            Toast.makeText(this@GroupViewActivity, "Failed to load expenses. Please try again later.", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Log.e("GroupViewActivity", "Unexpected error while fetching expenses", e)
            Toast.makeText(this@GroupViewActivity, "An unexpected error occurred.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun displayGroupMembers() {
        Log.d("GroupViewActivity", "Displaying group members.")
        val members = group?.members ?: emptyList()
        val memberAdapter = GroupMemberRecyclerAdapter(members)
        binding.groupActivityMembersRecyclerView.apply {
            adapter = memberAdapter
            layoutManager = LinearLayoutManager(this@GroupViewActivity)
        }
        Log.d("GroupViewActivity", "Group members displayed successfully.")
    }

    private fun navigateToLogin() {
        Log.d("GroupViewActivity", "Navigating to login.")
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}
