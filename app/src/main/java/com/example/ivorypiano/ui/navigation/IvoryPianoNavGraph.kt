package com.example.ivorypiano.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.ivorypiano.ui.session.*

/**
 * Provides Navigation graph for the application.
 */
@Composable
fun IvoryPianoNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = HomeDestination.route,
        modifier = modifier
    ) {
        composable(route = HomeDestination.route) {
            HomeScreen(
                navigateToSessionEntry = { navController.navigate(SessionEntryDestination.route) },
                navigateToSessionUpdate = { sessionId ->
                    navController.navigate("${SessionDetailsDestination.route}/$sessionId")
                }
            )
        }
        composable(route = SessionEntryDestination.route) {
            SessionEntryScreen(
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() }
            )
        }
        composable(
            route = SessionDetailsDestination.routeWithArgs,
            arguments = listOf(navArgument(SessionDetailsDestination.sessionIdArg) {
                type = NavType.IntType
            })
        ) {
            SessionDetailsScreen(
                navigateToEditSession = { /* sessionId -> navController.navigate("${SessionEditDestination.route}/$sessionId") */ },
                navigateBack = { navController.navigateUp() }
            )
        }
    }
}
