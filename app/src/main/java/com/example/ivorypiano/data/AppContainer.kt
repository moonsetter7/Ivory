package com.example.ivorypiano.data

import android.content.Context

/**
 * App container for Dependency injection
 */
interface AppContainer {
    val sessionsRepository: SessionsRepository
    val usersRepository: UsersRepository
}

/**
 * AppContainer implementation that provides instance of OfflineSessionsRepository
 * ...and OfflineUsersRepository
 */
class AppDataContainer(private val context: Context) : AppContainer {
    /**
     * Implementation for [SessionsRepository]
     */
    override val sessionsRepository: SessionsRepository by lazy {
        OfflineSessionsRepository(SessionDatabase.getDatabase(context).sessionDao())
    }

    /**
     * Implementation for [UsersRepository]
     */
    override val usersRepository: UsersRepository by lazy {
        OfflineUsersRepository(SessionDatabase.getDatabase(context).userDao())
    }
}
