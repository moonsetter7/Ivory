package com.example.ivorypiano.ui.session

import com.example.ivorypiano.R
import com.example.ivorypiano.ui.navigation.NavigationDestination

/**
 * Destination for Session Edit
 */
object SessionEditDestination : NavigationDestination {
    override val route = "session_edit"
    override val titleRes = R.string.session_edit_title
    const val sessionIdArg = "sessionId"
    val routeWithArgs = "$route/{$sessionIdArg}"
}
