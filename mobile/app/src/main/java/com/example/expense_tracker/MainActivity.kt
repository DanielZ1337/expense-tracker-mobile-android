package com.example.expense_tracker

import android.content.Intent
import android.net.http.HttpException
import android.os.Bundle
import android.util.Log
import android.view.Menu
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.example.expense_tracker.databinding.ActivityMainBinding
import com.example.expense_tracker.databinding.ActivityStartBinding;
import com.example.expense_tracker.network.ApiClient
import com.example.expense_tracker.network.repositories.AuthRepository
import com.example.expense_tracker.network.repositories.UserRepository
import com.example.expense_tracker.network.requests.LoginRequest
import com.example.expense_tracker.network.requests.RegisterRequest
import kotlinx.coroutines.launch
import java.io.IOException


class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityStartBinding
    private lateinit var authRepository: AuthRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ApiClient.init(this);

        authRepository = AuthRepository()




        val oldbinding = ActivityMainBinding.inflate(layoutInflater)
        binding = ActivityStartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //setSupportActionBar(binding.appBarMain.toolbar)


        binding.signUpButton.setOnClickListener { view ->
            val i : Intent = Intent(this, SignupActivity::class.java)
            startActivity(i)
        }

        binding.loginButton.setOnClickListener { view ->
            val i : Intent = Intent(this, LoginActivity::class.java)
            startActivity(i)
        }


        // Set up FAB to trigger API test

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun testApi() {
        lifecycleScope.launch {
            try {
                // Login with the new user
                val loginRequest = LoginRequest(
                    email = "test@example.com",
                    password = "password123"
                )
                val tokenResponse = authRepository.login(loginRequest)
                Log.d("MainActivity", "Login successful: $tokenResponse")

                // You can add more API tests here

                val userRepository = UserRepository()

                val userProfileResponse = userRepository.getUserProfile()
                Log.d("MainActivity", "User profile: $userProfileResponse")

            } catch (e: IOException) {
                Log.e("MainActivity", "Network error", e)
            } catch (e: HttpException) {
                Log.e("MainActivity", "HTTP error", e)
            } catch (e: Exception) {
                Log.e("MainActivity", "Unexpected error", e)
            }
        }
    }
}