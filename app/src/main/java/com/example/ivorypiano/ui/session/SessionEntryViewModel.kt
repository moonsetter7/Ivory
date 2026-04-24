package com.example.ivorypiano.ui.session

import com.example.ivorypiano.data.PianoSession

/*
Represents the UI State for a Piano session
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
    val pieceName: String = "",
    val composer: String = "",
    val bpm: String = "",
    val measures: String = "",
)

fun SessionDetails.toSession(): PianoSession = PianoSession(
    id = id,
    date = date,
    timestamp = timestamp,
    durationMillis = durationMillis,
    pieceName = pieceName,
    composer = composer,
    bpm = bpm,
    measures = measures,
)