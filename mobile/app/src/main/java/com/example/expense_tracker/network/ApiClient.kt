// NetworkModule.kt
package com.example.expense_tracker.network

import android.content.Context
import android.util.Log
import com.example.expense_tracker.adapters.LocalDateJsonAdapter
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
        .add(KotlinJsonAdapterFactory())
        .add(LocalDateJsonAdapter())
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
            .baseUrl("http://10.0.2.2:3000")
            .client(httpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()

        Log.d(null, "Initialized retrofit");
    }

    fun getRetrofit(): Retrofit {
        Log.d(null, "Getting retrofit");
        if (!::retrofit.isInitialized) {
            Log.d(null, "Retrofit not initialized");
            throw IllegalStateException("Retrofit not initialized")
        }

        Log.d(null, "Got retrofit client");
        return retrofit
    }
}
