package com.example.ivorypiano.ui.user

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ivorypiano.IvoryPianoTopAppBar
import com.example.ivorypiano.R
import com.example.ivorypiano.ui.AppViewModelProvider
import com.example.ivorypiano.ui.DevicePreviews
import com.example.ivorypiano.ui.navigation.NavigationDestination
import com.example.ivorypiano.ui.theme.IvoryPianoTheme
import kotlinx.coroutines.launch

object LoginDestination : NavigationDestination {
    override val route = "login"
    override val titleRes = R.string.login_title
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onRegisterClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val coroutineScope = rememberCoroutineScope()
    val loginUiState = viewModel.loginUiState
    
    LoginBody(
        loginUiState = loginUiState,
        onUpdateUiState = viewModel::updateUiState,
        onLoginClick = {
            coroutineScope.launch {
                if (viewModel.login()) {
                    onLoginSuccess()
                }
            }
        },
        onRegisterClick = onRegisterClick,
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginBody(
    loginUiState: LoginUiState,
    onUpdateUiState: (LoginDetails) -> Unit,
    onLoginClick: () -> Unit,
    onRegisterClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(stringResource(LoginDestination.titleRes)) }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(R.string.auth_welcome),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Text(
                text = stringResource(R.string.login_description),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            OutlinedTextField(
                value = loginUiState.loginDetails.username,
                onValueChange = { onUpdateUiState(loginUiState.loginDetails.copy(username = it)) },
                label = { Text(stringResource(R.string.username_label)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                isError = loginUiState.isLoginError
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = loginUiState.loginDetails.password,
                onValueChange = { onUpdateUiState(loginUiState.loginDetails.copy(password = it)) },
                label = { Text(stringResource(R.string.password_label)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                isError = loginUiState.isLoginError
            )

            if (loginUiState.isLoginError) {
                Text(
                    text = stringResource(R.string.login_failed),
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(top = 8.dp).align(Alignment.Start)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = onLoginClick,
                enabled = loginUiState.isLoginEnabled,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.login_action))
            }

            Spacer(modifier = Modifier.height(16.dp))

            TextButton(onClick = onRegisterClick) {
                Text(
                    text = stringResource(R.string.no_account_prompt) + " " + stringResource(R.string.register_link),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@DevicePreviews
@Composable
fun LoginScreenPreview() {
    IvoryPianoTheme {
        Surface(
            color = MaterialTheme.colorScheme.background
        ) {
            LoginBody(
                loginUiState = LoginUiState(),
                onUpdateUiState = {},
                onLoginClick = {},
                onRegisterClick = {}
            )
        }
    }
}
