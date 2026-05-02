package com.example.ivorypiano.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for piano practice sessions.
 */
@Dao
/*
This interface provides all the convenient methods for querying, inserting, deleting, and updating
the database.
 */
interface PianoSessionDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(session: PianoSession)

    @Update
    suspend fun update(session: PianoSession)

    @Delete
    suspend fun delete(session: PianoSession)

    @Query("SELECT * from practice_sessions WHERE id = :id")
    fun getSession(id: Int): Flow<PianoSession?>

    @Query("SELECT * from practice_sessions WHERE userId = :userId ORDER BY date DESC")
    fun getAllSessionsForUser(userId: Int): Flow<List<PianoSession>>

    @Query("SELECT * from practice_sessions ORDER BY date DESC")
    fun getAllSessions(): Flow<List<PianoSession>>
}
