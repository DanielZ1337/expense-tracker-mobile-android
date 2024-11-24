// MainActivity.kt
package com.example.expense_tracker

import android.content.Intent
import android.net.http.HttpException
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.expense_tracker.databinding.ActivityStartBinding
import com.example.expense_tracker.network.repositories.AuthRepository
import kotlinx.coroutines.launch
import java.io.IOException

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStartBinding
    private lateinit var authRepository: AuthRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize repositories
        authRepository = AuthRepository.getInstance()

        // Initially disable buttons to prevent user interaction until status is confirmed
        binding.loginButton.isEnabled = false
        binding.signUpButton.isEnabled = false

        // Optionally, show a progress bar while checking login status
        binding.progressBar.visibility = View.VISIBLE

        // Check if the user is logged in
        checkLoginStatus()

        // Set up click listeners for Login and Signup buttons
        binding.signUpButton.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }

        binding.loginButton.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun checkLoginStatus() {
        Log.d("MainActivity", "Checking login status")
        lifecycleScope.launch {
            try {
                val userProfile = authRepository.getSession()
                if (userProfile != null) {
                    Log.d("MainActivity", "User is logged in: ${userProfile.user.email}")
                    navigateToGroupManagement()
                } else {
                    Log.d("MainActivity", "User is not logged in or token expired")
                    showLoginOptions()
                }
            } catch (e: IOException) {
                Log.e("MainActivity", "Network error while checking session", e)
                Toast.makeText(this@MainActivity, "Network error. Please try again.", Toast.LENGTH_SHORT).show()
                showLoginOptions()
            } catch (e: HttpException) {
                Log.e("MainActivity", "HTTP error while checking session", e)
                Toast.makeText(this@MainActivity, "Authentication error. Please log in again.", Toast.LENGTH_SHORT).show()
                showLoginOptions()
            } catch (e: Exception) {
                Log.e("MainActivity", "Unexpected error while checking session", e)
                Toast.makeText(this@MainActivity, "An unexpected error occurred.", Toast.LENGTH_SHORT).show()
                showLoginOptions()
            } finally {
                binding.progressBar.visibility = View.GONE
            }
        }
    }

    private fun navigateToGroupManagement() {
        val intent = Intent(this, GroupManagementActivity::class.java)
        startActivity(intent)
        finish() // Prevent returning to this screen
    }

    private fun showLoginOptions() {
        binding.loginButton.isEnabled = true
        binding.signUpButton.isEnabled = true
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        // Implement navigation up if using Navigation Component
        return super.onSupportNavigateUp()
    }
}
