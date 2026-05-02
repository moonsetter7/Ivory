package com.example.ivorypiano.data

/**
 * Dummy data for use in Compose Previews.
 */
object DataSource {
    val dummySessions = listOf(
        PianoSession(
            id = 1,
            userId = 1,
            date = "2023-11-15",
            timestamp = 1700042400000L,
            durationMillis = 1800000L,
            pieceName = "Moonlight Sonata",
            composer = "Beethoven",
            bpm = "60",
            measures = "64"
        ),
        PianoSession(
            id = 2,
            userId = 1,
            date = "2024-03-20",
            timestamp = 1710932400000L,
            durationMillis = 2400000L,
            pieceName = "Clair de Lune",
            composer = "Debussy",
            bpm = "72",
            measures = "80"
        ),
        PianoSession(
            id = 3,
            userId = 1,
            date = "2025-07-04",
            timestamp = 1751623200000L,
            durationMillis = 3600000L,
            pieceName = "Nocturne Op. 9 No. 2",
            composer = "Chopin",
            bpm = "66",
            measures = "34"
        ),
        PianoSession(
            id = 4,
            userId = 1,
            date = "2026-01-10",
            timestamp = 1768041600000L,
            durationMillis = 1200000L,
            pieceName = "Minute Waltz",
            composer = "Chopin",
            bpm = "200",
            measures = "100"
        ),
        PianoSession(
            id = 5,
            userId = 1,
            date = "2026-01-11",
            timestamp = 1714665600000L,
            durationMillis = 420000L,
            pieceName = "Ondine",
            composer = "Maurice Ravel",
            bpm = "80",
            measures = "94"
        )
    )
}