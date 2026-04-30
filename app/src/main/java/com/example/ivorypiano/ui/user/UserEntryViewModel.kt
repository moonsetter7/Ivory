package com.example.ivorypiano.ui.user

import android.util.Patterns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.ivorypiano.data.SecurityManager
import com.example.ivorypiano.data.User
import com.example.ivorypiano.data.UserSessionRepository
import com.example.ivorypiano.data.UsersRepository

/**
 * ViewModel to validate and insert users into the Room database.
 */
class UserEntryViewModel(
    private val usersRepository: UsersRepository,
    private val userSessionRepository: UserSessionRepository,
    private val securityManager: SecurityManager
) : ViewModel() {

    /**
     * Holds current user ui state
     */
    var userUiState by mutableStateOf(UserUiState())
        private set

    /**
     * Updates the [userUiState] with the value provided in the argument.
     */
    fun updateUiState(userDetails: UserDetails) {
        val validation = validateInput(userDetails)
        userUiState = userUiState.copy(
            userDetails = userDetails,
            isEntryValid = validation.isValid,
            usernameError = validation.usernameError,
            emailError = validation.emailError,
            passwordError = validation.passwordError,
            isUsernameTaken = false, // Reset server-side errors on change
            isEmailTaken = false
        )
    }

    /**
     * Inserts a [User] into the Room database and sets the session ID.
     */
    suspend fun saveUser() {
        val details = userUiState.userDetails
        if (validateInput(details).isValid) {
            // Check for existing user
            val existingUsername = usersRepository.getUserByUsername(details.username)
            val existingEmail = usersRepository.getUserByEmail(details.email)

            if (existingUsername != null) {
                userUiState = userUiState.copy(isUsernameTaken = true, isEntryValid = false)
                return
            }

            if (existingEmail != null) {
                userUiState = userUiState.copy(isEmailTaken = true, isEntryValid = false)
                return
            }

            val hashedPassword = securityManager.hashPassword(details.password)
            val userToSave = details.toUser(hashedPassword)
            
            usersRepository.insertUser(userToSave)
            
            // Retrieve the saved user to get the ID
            val savedUser = usersRepository.getUserByUsername(details.username)
            if (savedUser != null) {
                userSessionRepository.setUserId(savedUser.id)
                securityManager.saveSession(savedUser.id, savedUser.username)
            }
        }
    }

    private fun validateInput(userDetails: UserDetails): ValidationResult {
        val usernameError = userDetails.username.isBlank()
        val emailError = userDetails.email.isBlank() || !Patterns.EMAIL_ADDRESS.matcher(userDetails.email).matches()
        val passwordError = userDetails.password.length < 6
        
        return ValidationResult(
            usernameError = usernameError,
            emailError = emailError,
            passwordError = passwordError
        )
    }
}

data class ValidationResult(
    val usernameError: Boolean,
    val emailError: Boolean,
    val passwordError: Boolean
) {
    val isValid: Boolean get() = !usernameError && !emailError && !passwordError
}

/**
 * Represents the UI State for a User.
 */
data class UserUiState(
    val userDetails: UserDetails = UserDetails(),
    val isEntryValid: Boolean = false,
    val usernameError: Boolean = false,
    val emailError: Boolean = false,
    val passwordError: Boolean = false,
    val isUsernameTaken: Boolean = false,
    val isEmailTaken: Boolean = false
)

data class UserDetails(
    val id: Int = 0,
    val username: String = "",
    val email: String = "",
    val password: String = ""
)

/**
 * Extension function converting UserDetails to User.
 */
fun UserDetails.toUser(hashedPassword: String): User = User(
    id = id,
    username = username,
    email = email,
    passwordHash = hashedPassword
)

/**
 * Extension function converting User to UserDetails.
 */
fun User.toUserDetails(): UserDetails = UserDetails(
    id = id,
    username = username,
    email = email,
    password = ""
)
