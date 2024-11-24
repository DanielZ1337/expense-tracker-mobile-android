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
import com.example.expense_tracker.databinding.ActivityLoginBinding
import com.example.expense_tracker.databinding.ActivityMainBinding
import com.example.expense_tracker.databinding.ActivitySignupBinding
import com.example.expense_tracker.models.TokenResponse
import com.example.expense_tracker.network.ApiClient
import com.example.expense_tracker.network.repositories.AuthRepository
import com.example.expense_tracker.network.repositories.UserRepository
import com.example.expense_tracker.network.requests.LoginRequest
import com.example.expense_tracker.network.requests.RegisterRequest
import com.example.expense_tracker.network.responses.UserProfileResponse
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import okio.IOException
import retrofit2.HttpException
import java.io.Serializable
import java.net.SocketTimeoutException

class LoginActivity : AppCompatActivity() {


    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityLoginBinding
    private lateinit var authRepository: AuthRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ApiClient.init(this);

        authRepository = AuthRepository()


        val oldbinding = ActivityMainBinding.inflate(layoutInflater)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //setSupportActionBar(binding.appBarMain.toolbar)

        /*
        binding.signUpButton.setOnClickListener { view ->
            val i: Intent = Intent(this, SignupActivity::class.java)
            startActivity(i)
        }*/

        binding.LoginActivityLogin.setOnClickListener{ view ->
            val emailText = binding.editTextTextEmail.text.toString()
            val passwordText = binding.editTextLoginPassword.text.toString()


            Log.d("SignupActivity", "Fields: " + emailText + " / " + passwordText)

            var responseToken: TokenResponse? = null
            var userProfileResponse: UserProfileResponse? = null

            lifecycleScope.launch {
                try {

                    val req = LoginRequest(emailText, passwordText)
                    responseToken = authRepository.login(req)

                    Log.d("MainActivity", "Login successful: $responseToken")

                    // You can add more API tests here

                    val userRepository = UserRepository()

                    userProfileResponse = userRepository.getUserProfile()
                    Log.d("MainActivity", "User profile: $userProfileResponse")


                } catch (e: IOException) {
                    Log.e("SignupActivity", "Network error", e)
                } catch (e: HttpException) {
                    Log.e("SignupActivity", "HTTP error", e)
                } catch (e: Exception) {
                    Log.e("SignupActivity", "Unexpected error", e)
                }
            }


            if (responseToken == null || userProfileResponse == null) {
                return@setOnClickListener
            }
            val i : Intent = Intent(this, LoginActivity::class.java)
            i.putExtra("token", responseToken!!.token as Serializable) // Needs to be serialisable to pass through intents
            i.putExtra("userProfile", userProfileResponse as Serializable)
            startActivity(i)




        }

        binding.buttonGoBack.setOnClickListener { view ->
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




