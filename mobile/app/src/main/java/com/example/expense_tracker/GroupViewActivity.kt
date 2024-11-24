package com.example.expense_tracker

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.ui.AppBarConfiguration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.expense_tracker.databinding.ActivityGroupManagementBinding
import com.example.expense_tracker.databinding.ActivityGroupViewBinding
import com.example.expense_tracker.models.Group
import com.example.expense_tracker.models.GroupExpense
import com.example.expense_tracker.models.TokenResponse
import com.example.expense_tracker.network.repositories.AuthRepository
import com.example.expense_tracker.network.repositories.ExpenseRepository
import com.example.expense_tracker.network.repositories.GroupRepository
import com.example.expense_tracker.network.responses.UserProfileResponse
import kotlinx.coroutines.launch
import okio.IOException
import retrofit2.HttpException


class GroupViewActivity : AppCompatActivity() {

    private lateinit var intent: Intent
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityGroupViewBinding
    private lateinit var authRepository: AuthRepository
    private lateinit var tokenResponse: TokenResponse
    private lateinit var userProfileResponse: UserProfileResponse
    private lateinit var groupRepository: GroupRepository
    private lateinit var expenseRepository: ExpenseRepository
    private lateinit var group: Group

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_group_view)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        authRepository = AuthRepository()
        groupRepository = GroupRepository()
        expenseRepository = ExpenseRepository()

        binding = ActivityGroupViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        intent = getIntent()
        tokenResponse = intent.extras?.get("token") as TokenResponse
        userProfileResponse = intent.extras?.get("userProfileResponse") as UserProfileResponse
        group = intent.extras?.get("group") as Group

        var expenses: List<GroupExpense>

        lifecycleScope.launch {
            try {
                expenses = groupRepository.getGroupExpenses(group.id)
                val members = group.members

                var expenseAdapter : GroupExpenseLogRecyclerAdapter = GroupExpenseLogRecyclerAdapter(expenses)
                binding.groupViewExpenseLogRecycler.adapter = expenseAdapter
                binding.groupViewExpenseLogRecycler.layoutManager = LinearLayoutManager(parent)

                var memberAdapter : GroupMemberRecyclerAdapter = GroupMemberRecyclerAdapter(members)
                binding.groupActivityMembersRecyclerView.adapter = memberAdapter
                binding.groupViewExpenseLogRecycler.layoutManager = LinearLayoutManager(parent)




            } catch (e: IOException) {
                Log.e("GroupManagementActivity", "Network error", e)
            } catch (e: HttpException) {
                Log.e("GroupManagementActivity", "HTTP error", e)
            } catch (e: Exception) {
                Log.e("GroupManagementActivity", "Unexpected error", e)
            }

            binding.groupGoBackButton.setOnClickListener { view ->
                finish()
            }

            binding.expensesButton.setOnClickListener { view ->

            }



        }
    }
}