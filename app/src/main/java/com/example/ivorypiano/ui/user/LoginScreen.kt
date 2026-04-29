package com.example.ivorypiano.ui.user

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ivorypiano.R
import com.example.ivorypiano.ui.AppViewModelProvider
import com.example.ivorypiano.ui.navigation.NavigationDestination
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
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = 32.dp)
            )
            
            OutlinedTextField(
                value = loginUiState.username,
                onValueChange = { viewModel.updateUiState(it) },
                label = { Text(stringResource(R.string.username_label)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            
            Spacer(modifier = Modifier.height(32.dp))
            
            Button(
                onClick = {
                    coroutineScope.launch {
                        if (viewModel.login()) {
                            onLoginSuccess()
                        }
                    }
                },
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
