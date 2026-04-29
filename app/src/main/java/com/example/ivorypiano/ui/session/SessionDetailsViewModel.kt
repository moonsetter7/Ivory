package com.example.ivorypiano.ui.session

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ivorypiano.data.SessionsRepository
import com.example.ivorypiano.data.UserSessionRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

/**
 * ViewModel to retrieve, update and delete a session from the [SessionsRepository]'s data source.
 */
class SessionDetailsViewModel(
    savedStateHandle: SavedStateHandle,
    private val sessionsRepository: SessionsRepository,
    private val userSessionRepository: UserSessionRepository,
) : ViewModel() {

    private val sessionId: Int = checkNotNull(savedStateHandle[SessionDetailsDestination.sessionIdArg])

    val uiState: StateFlow<SessionUiState> =
        sessionsRepository.getSessionStream(sessionId)
            .filterNotNull()
            .map {
                it.toSessionUiState(isEntryValid = true)
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = SessionUiState()
            )

    suspend fun deleteSession() {
        val userId = userSessionRepository.currentUserId.value ?: 0
        sessionsRepository.deleteSession(uiState.value.sessionDetails.toSession(userId))
    }

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}
