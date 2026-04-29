package com.example.ivorypiano.ui.user

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.ivorypiano.data.UserSessionRepository
import com.example.ivorypiano.data.UsersRepository
import kotlinx.coroutines.flow.firstOrNull

/**
 * ViewModel to handle login logic
 */
class LoginViewModel(
    private val usersRepository: UsersRepository,
    private val userSessionRepository: UserSessionRepository
) : ViewModel() {

    var loginUiState by mutableStateOf(LoginUiState())
        private set

    fun updateUiState(username: String) {
        loginUiState = LoginUiState(username = username, isLoginEnabled = username.isNotBlank())
    }

    suspend fun login(): Boolean {
        val users = usersRepository.getAllUsersStream().firstOrNull()
        val user = users?.find { it.username.equals(loginUiState.username, ignoreCase = true) }
        
        return if (user != null) {
            userSessionRepository.setUserId(user.id)
            true
        } else {
            false
        }
    }
}

data class LoginUiState(
    val username: String = "",
    val isLoginEnabled: Boolean = false
)
