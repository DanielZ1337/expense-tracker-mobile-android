package com.example.expense_tracker.network

import android.content.Context
import android.util.Log
import com.example.expense_tracker.adapters.InstantAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object ApiClient {

    private lateinit var retrofit: Retrofit
    lateinit var tokenProvider: TokenProvider
        private set

    private val moshi = Moshi.Builder()
        .add(InstantAdapter())
        .add(KotlinJsonAdapterFactory())
        .build()

    fun init(context: Context) {
        if (::retrofit.isInitialized) return

        tokenProvider = SecureTokenProvider.getInstance(context)

        val authInterceptor = AuthInterceptor(context)

        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val httpClient = OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(loggingInterceptor)
            .build()

        retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:3000") // Replace with your backend URL
            .client(httpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()

        Log.d("ApiClient", "Initialized Retrofit")
    }

    fun getRetrofit(): Retrofit {
        Log.d("ApiClient", "Getting Retrofit")
        if (!::retrofit.isInitialized) {
            Log.d("ApiClient", "Retrofit not initialized")
            throw IllegalStateException("Retrofit not initialized")
        }

        Log.d("ApiClient", "Got Retrofit client")
        return retrofit
    }
}
