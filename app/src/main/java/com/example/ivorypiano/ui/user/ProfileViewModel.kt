package com.example.ivorypiano.ui.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ivorypiano.data.SessionsRepository
import com.example.ivorypiano.data.User
import com.example.ivorypiano.data.UserSessionRepository
import com.example.ivorypiano.data.UsersRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn

class ProfileViewModel(
    private val usersRepository: UsersRepository,
    private val sessionsRepository: SessionsRepository,
    private val userSessionRepository: UserSessionRepository
) : ViewModel() {

    @OptIn(ExperimentalCoroutinesApi::class)
    val uiState: StateFlow<ProfileUiState> = userSessionRepository.currentUserId
        .flatMapLatest { id ->
            if (id != null) {
                combine(
                    usersRepository.getUserStream(id),
                    sessionsRepository.getAllSessionsForUserStream(id)
                ) { user, sessions ->
                    ProfileUiState(
                        user = user,
                        totalSessions = sessions.size,
                        totalDurationMillis = sessions.sumOf { it.durationMillis },
                        averageDurationMillis = if (sessions.isNotEmpty()) {
                            sessions.sumOf { it.durationMillis } / sessions.size
                        } else 0L
                    )
                }
            } else {
                flowOf(ProfileUiState())
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = ProfileUiState()
        )

    fun signOut() {
        userSessionRepository.clearSession()
    }
}

data class ProfileUiState(
    val user: User? = null,
    val totalSessions: Int = 0,
    val totalDurationMillis: Long = 0L,
    val averageDurationMillis: Long = 0L
)
