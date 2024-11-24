package com.example.expense_tracker

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.expense_tracker.databinding.ActivityGroupManagementBinding
import com.example.expense_tracker.models.Group
import com.example.expense_tracker.models.TokenResponse
import com.example.expense_tracker.network.repositories.AuthRepository
import com.example.expense_tracker.network.repositories.GroupRepository
import com.example.expense_tracker.network.responses.UserProfileResponse
import kotlinx.coroutines.launch
import okio.IOException
import retrofit2.HttpException
import java.io.Serializable

class GroupManagementActivity : AppCompatActivity() {


    private lateinit var intent: Intent
    private lateinit var binding: ActivityGroupManagementBinding
    private lateinit var authRepository: AuthRepository
    private lateinit var tokenResponse: TokenResponse
    private lateinit var userProfileResponse: UserProfileResponse
    private lateinit var groupRepository: GroupRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_group_management)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        authRepository = AuthRepository()
        groupRepository = GroupRepository()


        binding = ActivityGroupManagementBinding.inflate(layoutInflater)
        setContentView(binding.root)
        intent = getIntent()
        tokenResponse = intent.extras?.get("token") as TokenResponse
        userProfileResponse = intent.extras?.get("userProfileResponse") as UserProfileResponse

        binding.usernameLabel.text = userProfileResponse.name

        var groups: List<Group>

        lifecycleScope.launch {
            try {
                groups = groupRepository.getGroups()

                val adapter = GroupRecyclerViewAdapter(groups) { item: Group, position: Int ->
                    // This might seem weird. The constructor here takes a click listener for the individual entry as input.
                    Log.d("GroupManagementActivity", "Tapped Group ID ${item.id} with name ${item.name}")

                    val i = Intent(parent, GroupViewActivity::class.java)
                    i.putExtra("token", tokenResponse as Serializable) // Needs to be serialisable to pass through intents
                    i.putExtra("userProfile", userProfileResponse as Serializable)
                    i.putExtra("group", item as Serializable)
                    startActivity(i)

                }
                binding.groupsRecycler.adapter = adapter
                binding.groupsRecycler.layoutManager = LinearLayoutManager(parent)



            } catch (e: IOException) {
                Log.e("GroupManagementActivity", "Network error", e)
            } catch (e: HttpException) {
                Log.e("GroupManagementActivity", "HTTP error", e)
            } catch (e: Exception) {
                Log.e("GroupManagementActivity", "Unexpected error", e)
            }




        }


        binding.logoutButton.setOnClickListener { view ->
            finish()
        }

        binding.newGroupButton.setOnClickListener { view ->
            // TODO: Create New Group Activity
        }

    }



}