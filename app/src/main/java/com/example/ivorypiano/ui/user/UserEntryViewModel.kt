package com.example.ivorypiano.ui.user

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.ivorypiano.data.User
import com.example.ivorypiano.data.UsersRepository

/**
 * ViewModel to validate and insert users into the Room database.
 */
class UserEntryViewModel(private val usersRepository: UsersRepository) : ViewModel() {

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
        userUiState = UserUiState(
            userDetails = userDetails,
            isEntryValid = validateInput(userDetails)
        )
    }

    /**
     * Inserts a [User] into the Room database.
     */
    suspend fun saveUser() {
        if (validateInput(userUiState.userDetails)) {
            usersRepository.insertUser(userUiState.userDetails.toUser())
        }
    }

    private fun validateInput(uiState: UserDetails = userUiState.userDetails): Boolean {
        return with(uiState) {
            username.isNotBlank() && email.isNotBlank()
        }
    }
}

/**
 * Represents the UI State for a User.
 */
data class UserUiState(
    val userDetails: UserDetails = UserDetails(),
    val isEntryValid: Boolean = false
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
