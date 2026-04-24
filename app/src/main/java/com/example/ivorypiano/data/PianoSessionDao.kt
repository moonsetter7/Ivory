package com.example.ivorypiano.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

/*
Separating the persistence layer from the app using an abstract interface -- the DAO.
Hides all the complexities of involved in performing database operations in the underlying
persistence layer (Room in this case), separate from the rest of the application.
Allows you to change the data layer independently of the app using the data.
 */
@Dao
/*
This interface provides all the convenient methods for querying, inserting, deleting, and updating
the database.
 */
interface PianoSessionDao {
    // DAO functions are asynchronous since they take forever to execute.
    @Insert(onConflict = OnConflictStrategy.IGNORE) // if a new session conflicts, ignore it
    suspend fun insert(session: PianoSession)

    @Update
    suspend fun update(session: PianoSession)

    @Delete
    suspend fun delete(session: PianoSession)

    // Get all fields of a session based on its ID (primary key)
    @Query("SELECT * from practice_sessions WHERE id = :id")
    suspend fun getSession(id: Int): Flow<PianoSession>

    // Get all sessions in descending order by date
    @Query("SELECT * from practice_sessions ORDER BY date DESC")
    suspend fun getAllSessions(): Flow<List<PianoSession>>

}

