package com.example.ivorypiano.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.ivorypiano.ui.session.HomeDestination
import com.example.ivorypiano.ui.session.HomeScreen
import com.example.ivorypiano.ui.session.SessionDetailsDestination
import com.example.ivorypiano.ui.session.SessionDetailsScreen
import com.example.ivorypiano.ui.session.SessionEditDestination
import com.example.ivorypiano.ui.session.SessionEditScreen
import com.example.ivorypiano.ui.session.SessionEntryDestination
import com.example.ivorypiano.ui.session.SessionEntryScreen
import com.example.ivorypiano.ui.user.LoginDestination
import com.example.ivorypiano.ui.user.LoginScreen
import com.example.ivorypiano.ui.user.ProfileDestination
import com.example.ivorypiano.ui.user.ProfileScreen
import com.example.ivorypiano.ui.user.UserEntryDestination
import com.example.ivorypiano.ui.user.UserEntryScreen

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
        startDestination = LoginDestination.route,
        modifier = modifier
    ) {
        composable(route = LoginDestination.route) {
            LoginScreen(
                onLoginSuccess = {
                    /**
                     * Removes login screen from backstack once signed in
                     */
                    navController.navigate(HomeDestination.route) {
                        popUpTo(LoginDestination.route) { inclusive = true }
                    }
                },
                onRegisterClick = { navController.navigate(UserEntryDestination.route) }
            )
        }
        composable(route = UserEntryDestination.route) {
            UserEntryScreen(
                navigateBack = { navController.popBackStack() }
            )
        }
        composable(route = HomeDestination.route) {
            HomeScreen(
                navigateToSessionEntry = { navController.navigate(SessionEntryDestination.route) },
                navigateToSessionUpdate = { sessionId ->
                    navController.navigate("${SessionDetailsDestination.route}/$sessionId")
                },
                navigateToProfile = { navController.navigate(ProfileDestination.route) }
            )
        }
        composable(route = ProfileDestination.route) {
            ProfileScreen(
                onSignOut = {
                    navController.navigate(LoginDestination.route) {
                        popUpTo(HomeDestination.route) { inclusive = true }
                    }
                },
                navigateBack = { navController.popBackStack() }
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
                navigateToEditSession = { sessionId ->
                    navController.navigate("${SessionEditDestination.route}/$sessionId")
                },
                navigateBack = { navController.navigateUp() }
            )
        }
        composable(
            route = SessionEditDestination.routeWithArgs,
            arguments = listOf(navArgument(SessionEditDestination.sessionIdArg) {
                type = NavType.IntType
            })
        ) {
            SessionEditScreen(
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() }
            )
        }
    }
}
