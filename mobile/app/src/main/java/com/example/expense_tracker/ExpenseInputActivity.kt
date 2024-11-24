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
import com.example.expense_tracker.databinding.ActivityExpenseInputBinding
import com.example.expense_tracker.databinding.ActivityGroupManagementBinding
import com.example.expense_tracker.models.Group
import com.example.expense_tracker.models.TokenResponse
import com.example.expense_tracker.network.repositories.AuthRepository
import com.example.expense_tracker.network.repositories.GroupRepository
import com.example.expense_tracker.network.responses.UserProfileResponse
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import okio.IOException
import retrofit2.HttpException
import java.io.Serializable

class ExpenseInputActivity : AppCompatActivity() {

    private lateinit var intent: Intent
    private lateinit var binding: ActivityExpenseInputBinding
    private lateinit var authRepository: AuthRepository
    private lateinit var tokenResponse: TokenResponse
    private lateinit var userProfileResponse: UserProfileResponse
    private lateinit var groupRepository: GroupRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_expense_input)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        authRepository = AuthRepository.getInstance()
        groupRepository = GroupRepository()


        binding = ActivityExpenseInputBinding.inflate(layoutInflater)
        setContentView(binding.root)
        intent = getIntent()

        binding.payButton.setOnClickListener { view ->
            Snackbar.make(view, "Not Yet Implemented!", Snackbar.LENGTH_LONG)
        }

        binding.receiptsButton.setOnClickListener { view ->
            Snackbar.make(view, "Not Yet Implemented!", Snackbar.LENGTH_LONG)
        }

        binding.expenseInputGoBack.setOnClickListener { view ->
            finish()
        }

    }
}