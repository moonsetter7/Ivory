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
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(session: PianoSession)

    @Update
    suspend fun update(session: PianoSession)

    @Delete
    suspend fun delete(session: PianoSession)

    @Query("SELECT * from practice_sessions WHERE id = :id")
    fun getSession(id: Int): Flow<PianoSession>

    @Query("SELECT * from practice_sessions WHERE userId = :userId ORDER BY date DESC")
    fun getAllSessionsForUser(userId: Int): Flow<List<PianoSession>>

    @Query("SELECT * from practice_sessions ORDER BY date DESC")
    fun getAllSessions(): Flow<List<PianoSession>>
}
