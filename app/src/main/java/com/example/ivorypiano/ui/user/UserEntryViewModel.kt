package com.example.ivorypiano.ui.user

import android.util.Patterns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.ivorypiano.data.User
import com.example.ivorypiano.data.UserSessionRepository
import com.example.ivorypiano.data.UsersRepository
import kotlinx.coroutines.flow.firstOrNull

/**
 * ViewModel to validate and insert users into the Room database.
 */
class UserEntryViewModel(
    private val usersRepository: UsersRepository,
    private val userSessionRepository: UserSessionRepository
) : ViewModel() {

    /**
     * Holds current user ui state
     */
    var userUiState by mutableStateOf(UserUiState())
        private set

    /**
     * Updates the [userUiState] with the value provided in the argument. This method also triggers
     * a validation for input values.
     */
    fun updateUiState(userDetails: UserDetails) {
        val validation = validateInput(userDetails)
        userUiState = UserUiState(
            userDetails = userDetails,
            isEntryValid = validation.isValid,
            usernameError = validation.usernameError,
            emailError = validation.emailError
        )
    }

    /**
     * Inserts a [User] into the Room database and sets the session ID.
     */
    suspend fun saveUser() {
        if (validateInput(userUiState.userDetails).isValid) {
            usersRepository.insertUser(userUiState.userDetails.toUser())
            // Retrieve the user we just saved to get their ID
            val allUsers = usersRepository.getAllUsersStream().firstOrNull()
            val savedUser = allUsers?.find { it.username == userUiState.userDetails.username }
            if (savedUser != null) {
                userSessionRepository.setUserId(savedUser.id)
            }
        }
    }

    private fun validateInput(userDetails: UserDetails): ValidationResult {
        val usernameError = userDetails.username.isBlank()
        val emailError = userDetails.email.isBlank() || !Patterns.EMAIL_ADDRESS.matcher(userDetails.email).matches()
        
        return ValidationResult(
            usernameError = usernameError,
            emailError = emailError
        )
    }
}

data class ValidationResult(
    val usernameError: Boolean,
    val emailError: Boolean
) {
    val isValid: Boolean get() = !usernameError && !emailError
}

/**
 * Represents the UI State for a User.
 */
data class UserUiState(
    val userDetails: UserDetails = UserDetails(),
    val isEntryValid: Boolean = false,
    val usernameError: Boolean = false,
    val emailError: Boolean = false
)

data class UserDetails(
    val id: Int = 0,
    val username: String = "",
    val email: String = ""
)

/**
 * Extension function converting UserDetails to User.
 */
fun UserDetails.toUser(): User = User(
    id = id,
    username = username,
    email = email
)

/**
 * Extension function converting User to UserDetails.
 */
fun User.toUserDetails(): UserDetails = UserDetails(
    id = id,
    username = username,
    email = email
)

/**
 * Extension function converting User to User UI State.
 */
fun User.toUserUiState(isEntryValid: Boolean = false): UserUiState = UserUiState(
    userDetails = this.toUserDetails(),
    isEntryValid = isEntryValid
)
