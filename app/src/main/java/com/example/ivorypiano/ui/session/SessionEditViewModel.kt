package com.example.ivorypiano.ui.session

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ivorypiano.data.SessionsRepository
import com.example.ivorypiano.data.UserSessionRepository
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

/**
 * ViewModel to retrieve and update a session from the [SessionsRepository]'s data source.
 */
class SessionEditViewModel(
    savedStateHandle: SavedStateHandle,
    private val sessionsRepository: SessionsRepository,
    private val userSessionRepository: UserSessionRepository
) : ViewModel() {

    /**
     * Holds current session ui state
     */
    var sessionUiState by mutableStateOf(SessionUiState())
        private set

    private val sessionId: Int = checkNotNull(savedStateHandle[SessionEditDestination.sessionIdArg])

    init {
        viewModelScope.launch {
            val session = sessionsRepository.getSessionStream(sessionId)
                .filterNotNull()
                .first()
            sessionUiState = session.toSessionUiState(true).copy(
                timerMillis = session.durationMillis
            )
        }
    }

    /**
     * Updates the [sessionUiState] with the value provided in the argument. This method also triggers
     * a validation for input values.
     */
    fun updateUiState(sessionDetails: SessionDetails) {
        sessionUiState = sessionUiState.copy(
            sessionDetails = sessionDetails,
            isEntryValid = validateInput(sessionDetails)
        )
    }

    /**
     * Updates the session in the [SessionsRepository]'s data source
     */
    suspend fun updateSession() {
        if (validateInput(sessionUiState.sessionDetails)) {
            val userId = userSessionRepository.currentUserId.value ?: 0
            sessionsRepository.updateSession(sessionUiState.sessionDetails.toSession(userId))
        }
    }

    private fun validateInput(uiState: SessionDetails = sessionUiState.sessionDetails): Boolean {
        return true // Piece details are not mandatory in this app. I shouldn't have bothered,
        // but I'm this deep with validateInput integration in all of my viewmodels...

    }
}
