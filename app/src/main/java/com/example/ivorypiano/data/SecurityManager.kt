package com.example.ivorypiano.data

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import java.security.MessageDigest

/**
 * Handles basic data encryption and password hashing for Ivory Piano.
 */
class SecurityManager(context: Context) {

    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val sharedPreferences = EncryptedSharedPreferences.create(
        context,
        "secure_user_prefs",
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    /**
     * Hashes a password using SHA-256 for secure database storage.
     */
    fun hashPassword(password: String): String {
        val bytes = password.toByteArray()
        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(bytes)
        return digest.fold("") { str, it -> str + "%02x".format(it) }
    }

    /**
     * Securely stores the active session details.
     */
    fun saveSession(userId: Int, username: String) {
        sharedPreferences.edit()
            .putInt("last_user_id", userId)
            .putString("last_user_name", username)
            .apply()
    }

    /**
     * Retrieves the last logged-in user's ID.
     */
    fun getLastUserId(): Int {
        return sharedPreferences.getInt("last_user_id", -1)
    }

    /**
     * Retrieves the last logged-in user's name.
     */
    fun getLastUserName(): String? {
        return sharedPreferences.getString("last_user_name", null)
    }

    /**
     * Clears the stored session (for logout functionality later).
     */
    fun clearSession() {
        sharedPreferences.edit().clear().apply()
    }
}
