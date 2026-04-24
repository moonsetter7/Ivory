package com.example.ivorypiano.data

import android.content.Context

/**
 * App container for Dependency injection
 */
interface AppContainer {
    val sessionsRepository: SessionsRepository
}

/**
 * AppContainer implementation that provides instance of OfflineSessionsRepository
 */

class AppDataContainer(private val context: Context) : AppContainer {
    /**
     * Implementation for [SessionsRepository]
     * Instantiates the database instance for the app by calling getDatabase on the SessionDatabase
     * class, passing in the context and calling .sessionDao() to instantiate the DAO.
     */
    override val sessionsRepository: SessionsRepository by lazy {
        OfflineSessionsRepository(SessionDatabase.getDatabase(context).sessionDao())
    }
}