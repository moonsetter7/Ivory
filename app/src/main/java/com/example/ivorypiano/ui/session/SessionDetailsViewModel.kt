package com.example.ivorypiano.ui.session

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ivorypiano.data.SessionsRepository
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
        sessionsRepository.deleteSession(uiState.value.sessionDetails.toSession())
    }

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}

object SessionDetailsDestination {
    const val sessionIdArg = "sessionId"
}
