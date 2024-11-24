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
import com.example.expense_tracker.databinding.ActivityGroupManagementBinding
import com.example.expense_tracker.models.Group
import com.example.expense_tracker.network.repositories.AuthRepository
import com.example.expense_tracker.network.repositories.GroupRepository
import com.example.expense_tracker.ui.adapters.GroupRecyclerViewAdapter
import com.example.expense_tracker.ui.groups.CreateGroupDialog
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class GroupManagementActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGroupManagementBinding
    private lateinit var authRepository: AuthRepository
    private lateinit var groupRepository: GroupRepository
    private lateinit var groupAdapter: GroupRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityGroupManagementBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.d("GroupManagementActivity", "onCreate called.")

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        authRepository = AuthRepository.getInstance()
        Log.d("GroupManagementActivity", "AuthRepository initialized.")

        groupRepository = GroupRepository()
        Log.d("GroupManagementActivity", "GroupRepository initialized.")

        groupAdapter = GroupRecyclerViewAdapter(emptyList()) { group: Group, position: Int ->
            Log.d("GroupManagementActivity", "Tapped Group ID ${group.id} with name ${group.name}")

            val intent = Intent(this, GroupViewActivity::class.java)
            intent.putExtra("groupId", group.id)
            startActivity(intent)
        }

        binding.groupsRecycler.adapter = groupAdapter
        binding.groupsRecycler.layoutManager = LinearLayoutManager(this)
        Log.d("GroupManagementActivity", "RecyclerView initialized.")

        binding.logoutButton.setOnClickListener {
            Log.d("GroupManagementActivity", "Logout button clicked.")
            performLogout()
        }

        binding.newGroupButton.setOnClickListener {
            Log.d("GroupManagementActivity", "New Group button clicked.")
            showCreateGroupDialog()
        }

        fetchUserProfileAndGroups()
    }

    private fun showCreateGroupDialog() {
        Log.d("GroupManagementActivity", "Showing CreateGroupDialog.")
        val dialog = CreateGroupDialog(this, groupRepository)
        dialog.show()
    }

    fun refreshGroups() {
        Log.d("GroupManagementActivity", "Refreshing groups.")
        lifecycleScope.launch {
            try {
                val groups: List<Group> = groupRepository.getGroups()
                groupAdapter.updateGroups(groups)
                Log.d("GroupManagementActivity", "Groups refreshed successfully. Total groups: ${groups.size}")
            } catch (e: IOException) {
                Log.e("GroupManagementActivity", "Network error while refreshing groups", e)
                Toast.makeText(this@GroupManagementActivity, "Failed to load groups. Please try again.", Toast.LENGTH_SHORT).show()
            } catch (e: HttpException) {
                Log.e("GroupManagementActivity", "HTTP error while refreshing groups", e)
                Toast.makeText(this@GroupManagementActivity, "Failed to load groups. Please try again.", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Log.e("GroupManagementActivity", "Unexpected error while refreshing groups", e)
                Toast.makeText(this@GroupManagementActivity, "An unexpected error occurred.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun fetchUserProfileAndGroups() {
        Log.d("GroupManagementActivity", "Fetching user profile and groups.")
        lifecycleScope.launch {
            try {
                val userProfileResponse = authRepository.getSession()
                if (userProfileResponse != null) {
                    Log.d("GroupManagementActivity", "User Profile: ${userProfileResponse.user.name}")
                    binding.usernameLabel.text = userProfileResponse.user.name

                    fetchGroups()
                } else {
                    Log.e("GroupManagementActivity", "Session expired or user not logged in.")
                    Toast.makeText(this@GroupManagementActivity, "Session expired. Please log in again.", Toast.LENGTH_SHORT).show()
                    navigateToLogin()
                }
            } catch (e: IOException) {
                Log.e("GroupManagementActivity", "Network error", e)
                Toast.makeText(this@GroupManagementActivity, "Network error. Please try again.", Toast.LENGTH_SHORT).show()
            } catch (e: HttpException) {
                Log.e("GroupManagementActivity", "HTTP error", e)
                Toast.makeText(this@GroupManagementActivity, "Authentication error. Please log in again.", Toast.LENGTH_SHORT).show()
                navigateToLogin()
            } catch (e: Exception) {
                Log.e("GroupManagementActivity", "Unexpected error", e)
                Toast.makeText(this@GroupManagementActivity, "An unexpected error occurred.", Toast.LENGTH_SHORT).show()
                navigateToLogin()
            }
        }
    }

    private suspend fun fetchGroups() {
        Log.d("GroupManagementActivity", "Fetching groups.")
        try {
            val groups: List<Group> = groupRepository.getGroups()
            groupAdapter.updateGroups(groups)
            Log.d("GroupManagementActivity", "Groups fetched successfully. Total groups: ${groups.size}")
        } catch (e: IOException) {
            Log.e("GroupManagementActivity", "Network error while fetching groups", e)
            Toast.makeText(this@GroupManagementActivity, "Failed to load groups. Please try again.", Toast.LENGTH_SHORT).show()
        } catch (e: HttpException) {
            Log.e("GroupManagementActivity", "HTTP error while fetching groups", e)
            Toast.makeText(this@GroupManagementActivity, "Failed to load groups. Please try again.", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Log.e("GroupManagementActivity", "Unexpected error while fetching groups", e)
            Toast.makeText(this@GroupManagementActivity, "An unexpected error occurred.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun performLogout() {
        Log.d("GroupManagementActivity", "Performing logout.")
        authRepository.logout()
        Toast.makeText(this, "Logged out successfully.", Toast.LENGTH_SHORT).show()
        navigateToLogin()
    }

    private fun navigateToLogin() {
        Log.d("GroupManagementActivity", "Navigating to login.")
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}
