package com.example.expense_tracker

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import com.example.expense_tracker.databinding.ActivityMainBinding
import com.example.expense_tracker.databinding.ActivitySignupBinding
import com.example.expense_tracker.network.ApiClient
import com.example.expense_tracker.network.repositories.AuthRepository
import com.example.expense_tracker.network.requests.RegisterRequest
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import okio.IOException
import retrofit2.HttpException
import java.net.SocketTimeoutException

class SignupActivity : AppCompatActivity() {


    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivitySignupBinding
    private lateinit var authRepository: AuthRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ApiClient.init(this);

        authRepository = AuthRepository.getInstance()


        val oldbinding = ActivityMainBinding.inflate(layoutInflater)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //setSupportActionBar(binding.appBarMain.toolbar)

        /*
        binding.signUpButton.setOnClickListener { view ->
            val i: Intent = Intent(this, SignupActivity::class.java)
            startActivity(i)
        }*/

        binding.signUpActivitySignupButton.setOnClickListener { view ->
            val usernameText = binding.editTextUsername.text.toString()
            val emailText = binding.editTextTextEmailAddress.text.toString()
            val passwordText = binding.editTextTextPassword.text.toString()
            val confirmPasswordText = binding.editTextConfirmPassword.text.toString()

            if (passwordText != confirmPasswordText) {
                Snackbar.make(view, "Passwords must be identical!", Snackbar.LENGTH_LONG).show()
                return@setOnClickListener
            }

            Log.d("SignupActivity", "Fields: " + usernameText + " / " + emailText + " / " + passwordText)

            lifecycleScope.launch {
                try {

                    val req = RegisterRequest(emailText,passwordText,usernameText, confirmPasswordText)
                    val res = authRepository.register(req)


                } catch (e: IOException) {
                    Log.e("SignupActivity", "Network error", e)
                } catch (e: HttpException) {
                    Log.e("SignupActivity", "HTTP error", e)
                } catch (e: Exception) {
                    Log.e("SignupActivity", "Unexpected error", e)
                }
            }

        }

        binding.SignUpActivityBack.setOnClickListener { view ->
            finish()
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

}




