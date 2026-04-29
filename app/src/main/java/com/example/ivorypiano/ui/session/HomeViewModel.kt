package com.example.ivorypiano.ui.session

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ivorypiano.data.PianoSession
import com.example.ivorypiano.data.SessionsRepository
import com.example.ivorypiano.data.UserSessionRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn

/**
 * ViewModel to retrieve and aggregate sessions from the Room database for the active user.
 */
class HomeViewModel(
    private val sessionsRepository: SessionsRepository,
    private val userSessionRepository: UserSessionRepository
) : ViewModel() {

    private val _aggregationType = MutableStateFlow(AggregationType.NONE)
    val aggregationType: StateFlow<AggregationType> = _aggregationType

    /**
     * Holds home ui state. The list of sessions are retrieved for the active user
     * and aggregated based on the selected [AggregationType].
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    val homeUiState: StateFlow<HomeUiState> = userSessionRepository.currentUserId
        .flatMapLatest { userId ->
            if (userId == null) {
                flowOf(emptyList<PianoSession>())
            } else {
                sessionsRepository.getAllSessionsForUserStream(userId)
            }
        }
        .combine(_aggregationType) { sessions, type ->
            HomeUiState(
                sessionList = sessions,
                aggregatedData = aggregateSessions(sessions, type),
                aggregationType = type
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
            initialValue = HomeUiState()
        )

    fun setAggregationType(type: AggregationType) {
        _aggregationType.value = type
    }

    private fun aggregateSessions(
        sessions: List<PianoSession>,
        type: AggregationType
    ): Map<String, List<PianoSession>> {
        return when (type) {
            AggregationType.PIECE -> sessions.groupBy { it.pieceName }
            AggregationType.COMPOSER -> sessions.groupBy { it.composer }
            AggregationType.MONTH -> sessions.groupBy { it.date.substringBeforeLast("-") }
            AggregationType.NONE -> emptyMap()
        }
    }

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}

enum class AggregationType {
    NONE, PIECE, COMPOSER, MONTH
}

/**
 * Ui State for HomeScreen
 */
data class HomeUiState(
    val sessionList: List<PianoSession> = listOf(),
    val aggregatedData: Map<String, List<PianoSession>> = emptyMap(),
    val aggregationType: AggregationType = AggregationType.NONE
)
