package com.example.ivorypiano.data

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Repository that manages the current user session.
 * For this implementation, it's kept in memory.
 */
interface UserSessionRepository {
    val currentUserId: StateFlow<Int?>
    fun setUserId(id: Int?)
}

class InMemoryUserSessionRepository : UserSessionRepository {
    private val _currentUserId = MutableStateFlow<Int?>(null)
    override val currentUserId: StateFlow<Int?> = _currentUserId.asStateFlow()

    override fun setUserId(id: Int?) {
        _currentUserId.value = id
    }
}