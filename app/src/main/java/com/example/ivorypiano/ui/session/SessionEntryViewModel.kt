package com.example.ivorypiano.ui.session

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.ivorypiano.data.PianoSession
import com.example.ivorypiano.data.SessionsRepository

/**
 * ViewModel to validate and insert sessions into the room database
 */
class SessionEntryViewModel(private val sessionsRepository: SessionsRepository) : ViewModel() {

    /**
     * Holds current session ui state
     */
    var sessionUiState by mutableStateOf(SessionUiState())
        private set

    /**
     * Updates the [sessionUiState] with the value provided in the argument. This method also triggers
     * a validation for input values.
     */
    fun updateUiState(sessionDetails: SessionDetails) {
        sessionUiState =
            SessionUiState(sessionDetails = sessionDetails, isEntryValid = validateInput(sessionDetails))
    }

    /**
     * Inserts a [PianoSession] into the Room database
     */
    suspend fun saveSession() {
        if (validateInput(sessionUiState.sessionDetails)) {
            sessionsRepository.insertSession(sessionUiState.sessionDetails.toSession())
        }
    }

    private fun validateInput(uiState: SessionDetails = sessionUiState.sessionDetails): Boolean {
        return with(uiState) {
            date.isNotBlank() &&
            timestamp.toString().isNotBlank() &&
            durationMillis.toString().isNotBlank()
        }
    }
}

/**
 * Represents the UI State for a Piano session
 */
data class SessionUiState(
    val sessionDetails: SessionDetails = SessionDetails(),
    val isEntryValid: Boolean = false
)

data class SessionDetails(
    val id: Int = 0,
    val date: String = "",
    val timestamp: Long = 0L,
    val durationMillis: Long = 0L,
    val pieceName: String? = "",
    val composer: String? = "",
    val bpm: String? = "",
    val measures: String? = "",
)

/**
 * Extension function converting SessionDetails to PianoSession
 * Saving a piano piece in a session is optional, therefore its details are optional,
 * and thus nullable
 */

fun SessionDetails.toSession(): PianoSession = PianoSession(
    id = id,
    date = date,
    timestamp = timestamp,
    durationMillis = durationMillis,
    pieceName = pieceName ?: "",
    composer = composer ?: "",
    bpm = bpm ?: "",
    measures = measures ?: "",
)

/**
 * Extension function converting PianoSession to SessionDetails
 */
fun PianoSession.toSessionDetails(): SessionDetails = SessionDetails(
    id = id,
    date = date,
    timestamp = timestamp,
    durationMillis = durationMillis,
    pieceName = pieceName,
    composer = composer,
    bpm = bpm,
    measures = measures,
)

/**
 * Extension function converting PianoSession to Session UI State
 */
fun PianoSession.toSessionUiState(isEntryValid: Boolean = false): SessionUiState = SessionUiState(
    sessionDetails = this.toSessionDetails(),
    isEntryValid = isEntryValid
)
