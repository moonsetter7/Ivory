package com.example.ivorypiano.ui.session

import com.example.ivorypiano.R
import com.example.ivorypiano.ui.navigation.NavigationDestination

/**
 * Destination for Session Details
 */
object SessionDetailsDestination : NavigationDestination {
    override val route = "session_details"
    override val titleRes = R.string.session_details_title
    const val sessionIdArg = "sessionId"
    val routeWithArgs = "$route/{$sessionIdArg}"
}
