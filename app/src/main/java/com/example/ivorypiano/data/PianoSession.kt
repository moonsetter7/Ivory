package com.example.ivorypiano.data

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
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
    @PrimaryKey(autoGenerate = true) val id: Int = 0, // auto-increment
    //mark non-null to prevent app from accidentally saving a corrupted session
    @NonNull val date: String,
    @NonNull val timestamp: ULong,
    @NonNull val durationMillis: ULong,
    val pieceName: String,
    val composer: String,
    val bpm: String,
    val measures: String,
)
