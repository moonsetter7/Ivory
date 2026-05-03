package com.example.ivorypiano.ui.user

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ivorypiano.R
import com.example.ivorypiano.ui.AppViewModelProvider
import com.example.ivorypiano.ui.DevicePreviews
import com.example.ivorypiano.ui.navigation.NavigationDestination
import com.example.ivorypiano.ui.theme.IvoryPianoTheme
import kotlinx.coroutines.launch

object UserEntryDestination : NavigationDestination {
    override val route = "user_registration"
    override val titleRes = R.string.register_title
}

@Composable
fun UserEntryScreen(
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: UserEntryViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val coroutineScope = rememberCoroutineScope()
    UserEntryScreenContent(
        userUiState = viewModel.userUiState,
        onUserValueChange = viewModel::updateUiState,
        onSaveClick = {
            coroutineScope.launch {
                viewModel.saveUser()
                // Only navigate back if the user was successfully established
                if (viewModel.userUiState.isEntryValid) {
                    navigateBack()
                }
            }
        },
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserEntryScreenContent(
    userUiState: UserUiState,
    onUserValueChange: (UserDetails) -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(stringResource(UserEntryDestination.titleRes)) }
            )
        }
    ) { innerPadding ->
        UserEntryBody(
            userUiState = userUiState,
            onUserValueChange = onUserValueChange,
            onSaveClick = onSaveClick,
            modifier = modifier.padding(innerPadding)
        )
    }
}

@Composable
fun UserEntryBody(
    userUiState: UserUiState,
    onUserValueChange: (UserDetails) -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.register_description),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        OutlinedTextField(
            value = userUiState.userDetails.username,
            onValueChange = { onUserValueChange(userUiState.userDetails.copy(username = it)) },
            label = { Text(stringResource(R.string.username_label)) },
            modifier = Modifier.fillMaxWidth(),
            isError = userUiState.usernameError || userUiState.isUsernameTaken,
            supportingText = {
                if (userUiState.isUsernameTaken) {
                    Text(stringResource(R.string.username_taken))
                } else if (userUiState.usernameError) {
                    Text(stringResource(R.string.invalid_username))
                }
            },
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = userUiState.userDetails.email,
            onValueChange = { onUserValueChange(userUiState.userDetails.copy(email = it)) },
            label = { Text(stringResource(R.string.email_label)) },
            modifier = Modifier.fillMaxWidth(),
            isError = userUiState.emailError || userUiState.isEmailTaken,
            supportingText = {
                if (userUiState.isEmailTaken) {
                    Text(stringResource(R.string.email_taken))
                } else if (userUiState.emailError) {
                    Text(stringResource(R.string.invalid_email))
                }
            },
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = userUiState.userDetails.password,
            onValueChange = { onUserValueChange(userUiState.userDetails.copy(password = it)) },
            label = { Text(stringResource(R.string.password_label)) },
            modifier = Modifier.fillMaxWidth(),
            isError = userUiState.passwordError,
            /**
             * Masks the password field from onlookers
             */
            visualTransformation = PasswordVisualTransformation(),
            supportingText = {
                if (userUiState.passwordError) {
                    Text(stringResource(R.string.invalid_password))
                }
            },
            singleLine = true
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = onSaveClick,
            enabled = userUiState.isEntryValid,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.register_action))
        }
    }
}

@DevicePreviews
@Composable
fun UserEntryPreview() {
    IvoryPianoTheme {
        UserEntryScreenContent(
            userUiState = UserUiState(),
            onUserValueChange = {},
            onSaveClick = {}
        )
    }
}
