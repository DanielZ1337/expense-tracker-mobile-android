package com.example.expense_tracker.network

import android.annotation.SuppressLint
import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

class SecureTokenProvider private constructor(context: Context) : TokenProvider {

    companion object {
        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var INSTANCE: SecureTokenProvider? = null

        fun getInstance(context: Context): SecureTokenProvider {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: SecureTokenProvider(context.applicationContext).also { INSTANCE = it }
            }
        }
    }

    private val prefsFileName = "auth_prefs"

    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val prefs = EncryptedSharedPreferences.create(
        context,
        prefsFileName,
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    private val tokenKey = "auth_token"

    override fun getToken(): String? {
        return prefs.getString(tokenKey, null)
    }

    override fun saveToken(token: String) {
        prefs.edit().putString(tokenKey, token).apply()
    }

    override fun clearToken() {
        prefs.edit().remove(tokenKey).apply()
    }
}
