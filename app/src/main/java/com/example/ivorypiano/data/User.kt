package com.example.ivorypiano.data

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Represents a user of the app.
 */
@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val username: String,
    val email: String,
    val passwordHash: String
)
