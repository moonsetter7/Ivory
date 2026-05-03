package com.example.ivorypiano.ui.user

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.ivorypiano.data.SecurityManager
import com.example.ivorypiano.data.UserSessionRepository
import com.example.ivorypiano.data.UsersRepository
import kotlinx.coroutines.flow.firstOrNull

/**
 * ViewModel to handle secure login logic.
 */
class LoginViewModel(
    private val usersRepository: UsersRepository,
    private val userSessionRepository: UserSessionRepository,
    private val securityManager: SecurityManager
) : ViewModel() {

    /**
     * Initialize UI state with the last logged-in artist from secure storage.
     */
    var loginUiState by mutableStateOf(
        LoginUiState(
            loginDetails = LoginDetails(username = securityManager.getLastUserName() ?: "")
        )
    )
        private set

    fun updateUiState(loginDetails: LoginDetails) {
        loginUiState = loginUiState.copy(
            loginDetails = loginDetails,
            isLoginEnabled = loginDetails.username.isNotBlank() && loginDetails.password.isNotBlank(),
            isLoginError = false // Reset error when user starts typing
        )
    }

    /**
     * Verifies credentials and establishes a user session.
     */
    suspend fun login(): Boolean {
        val users = usersRepository.getAllUsersStream().firstOrNull()
        val user = users?.find {
            it.username.equals(
                loginUiState.loginDetails.username,
                ignoreCase = true
            )
        }

        val isSuccess = if (user != null) {
            val hashedInput = securityManager.hashPassword(loginUiState.loginDetails.password)
            if (user.passwordHash == hashedInput) {
                userSessionRepository.setUserId(user.id)
                securityManager.saveSession(user.id, user.username)
                true
            } else {
                false
            }
        } else {
            false
        }

        if (!isSuccess) {
            loginUiState = loginUiState.copy(isLoginError = true)
        }

        return isSuccess
    }
}

data class LoginDetails(
    val username: String = "",
    val password: String = ""
)

data class LoginUiState(
    val loginDetails: LoginDetails = LoginDetails(),
    val isLoginEnabled: Boolean = false,
    val isLoginError: Boolean = false
)
