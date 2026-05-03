package com.example.ivorypiano.data

import android.content.Context

/**
 * App container for Dependency injection
 */
interface AppContainer {
    val sessionsRepository: SessionsRepository
    val usersRepository: UsersRepository
    val userSessionRepository: UserSessionRepository
    val securityManager: SecurityManager
}

/**
 * AppContainer implementation that provides instances of repositories
 */
class AppDataContainer(private val context: Context) : AppContainer {

    override val sessionsRepository: SessionsRepository by lazy {
        OfflineSessionsRepository(SessionDatabase.getDatabase(context).sessionDao())
    }

    override val usersRepository: UsersRepository by lazy {
        OfflineUsersRepository(SessionDatabase.getDatabase(context).userDao())
    }

    /**
     * Hoisted app-level state for the active user session.
     */
    override val userSessionRepository: UserSessionRepository by lazy {
        InMemoryUserSessionRepository()
    }

    override val securityManager: SecurityManager by lazy {
        SecurityManager(context)
    }
}
