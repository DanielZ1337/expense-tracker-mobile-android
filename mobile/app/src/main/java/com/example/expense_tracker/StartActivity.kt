package com.example.expense_tracker


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
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.example.expense_tracker.databinding.ActivityMainBinding
import com.example.expense_tracker.network.ApiClient
import com.example.expense_tracker.network.repositories.AuthRepository
import com.example.expense_tracker.network.repositories.UserRepository
import com.example.expense_tracker.network.requests.LoginRequest
import com.example.expense_tracker.network.requests.RegisterRequest
import kotlinx.coroutines.launch
import java.io.IOException


class StartActivity : AppCompatActivity() {




}