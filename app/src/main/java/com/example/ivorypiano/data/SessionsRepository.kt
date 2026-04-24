package com.example.ivorypiano.data

import kotlinx.coroutines.flow.Flow

/*
Repository that provides insert, update, delete, and retrieve of a session from a given data source
 */
interface SessionsRepository {
    /*
    Retrieve all sessions from the given data source
     */
    fun getAllSessionsStream(): Flow<List<PianoSession>>

    /*
    Retrieve a session from the given data source based on its ID
     */
    fun getSessionStream(id: Int): Flow<PianoSession?>

    /*
    Insert session into a data source
     */
    suspend fun insertSession(session: PianoSession)

    /*
    Delete session from the data source
     */
    suspend fun deleteSession(session: PianoSession)

    /*
    Update session in the data source
     */
    suspend fun updateSession(session: PianoSession)
}