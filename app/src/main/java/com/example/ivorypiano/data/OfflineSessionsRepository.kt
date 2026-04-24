package com.example.ivorypiano.data

import kotlinx.coroutines.flow.Flow

class OfflineSessionsRepository(private val sessionDao: PianoSessionDao) : SessionsRepository {
    override fun getAllSessionsStream(): Flow<List<PianoSession>> = sessionDao.getAllSessions()

    override fun getSessionStream(id: Int): Flow<PianoSession?> = sessionDao.getSession(id)

    override suspend fun insertSession(session: PianoSession) = sessionDao.insert(session)

    override suspend fun deleteSession(session: PianoSession) = sessionDao.delete(session)

    override suspend fun updateSession(session: PianoSession) = sessionDao.update(session)
}
