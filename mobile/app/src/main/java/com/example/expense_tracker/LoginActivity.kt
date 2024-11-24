// LoginActivity.kt
package com.example.expense_tracker

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.expense_tracker.databinding.ActivityLoginBinding
import com.example.expense_tracker.network.repositories.AuthRepository
import com.example.expense_tracker.network.requests.LoginRequest
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var authRepository: AuthRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        authRepository = AuthRepository.getInstance()

        binding.LoginActivityLogin.setOnClickListener {
            val email = binding.editTextTextEmail.text.toString().trim()
            val password = binding.editTextLoginPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            performLogin(email, password)
        }
    }

    private fun performLogin(email: String, password: String) {
        binding.progressBar.visibility = View.VISIBLE
        lifecycleScope.launch {
            try {
                val loginRequest = LoginRequest(email = email, password = password)
                val tokenResponse = authRepository.login(loginRequest)
                Log.d("LoginActivity", "Login successful: ${tokenResponse.token}")

                // Navigate to GroupManagementActivity
                val intent = Intent(this@LoginActivity, GroupManagementActivity::class.java)
                startActivity(intent)
                finish()

            } catch (e: IOException) {
                Log.e("LoginActivity", "Network error", e)
                Toast.makeText(this@LoginActivity, "Network error. Please try again.", Toast.LENGTH_SHORT).show()
            } catch (e: HttpException) {
                Log.e("LoginActivity", "HTTP error", e)
                if (e.code() == 401) {
                    Toast.makeText(this@LoginActivity, "Invalid email or password.", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@LoginActivity, "Authentication failed. Please try again.", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Log.e("LoginActivity", "Unexpected error", e)
                Toast.makeText(this@LoginActivity, "An unexpected error occurred.", Toast.LENGTH_SHORT).show()
            } finally {
                binding.progressBar.visibility = View.GONE
            }
        }
    }
}
