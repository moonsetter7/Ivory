package com.example.ivorypiano.data

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Represents a single table in the database. Each row is a separate instance of
 * the PianoSession class. Each property corresponds to a column.
 * Additionally, an ID is needed as a unique identifier for
 * each row in the database.
 */
@Entity(tableName = "practice_sessions")
data class PianoSession(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val userId: Int = 0, // Links session to a specific user
    @NonNull val date: String,
    @NonNull val timestamp: Long,
    @NonNull val durationMillis: Long,
    val pieceName: String,
    val composer: String,
    val bpm: String,
    val measures: String,
)
