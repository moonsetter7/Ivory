package com.example.ivorypiano.ui.user

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
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

object UserEntryDestination : NavigationDestination {
    override val route = "user_registration"
    override val titleRes = R.string.register_title
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserEntryScreen(
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: UserEntryViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val coroutineScope = rememberCoroutineScope()
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(stringResource(UserEntryDestination.titleRes)) }
            )
        }
    ) { innerPadding ->
        UserEntryBody(
            userUiState = viewModel.userUiState,
            onUserValueChange = viewModel::updateUiState,
            onSaveClick = {
                coroutineScope.launch {
                    viewModel.saveUser()
                    navigateBack()
                }
            },
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
            singleLine = true
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        OutlinedTextField(
            value = userUiState.userDetails.email,
            onValueChange = { onUserValueChange(userUiState.userDetails.copy(email = it)) },
            label = { Text(stringResource(R.string.email_label)) },
            modifier = Modifier.fillMaxWidth(),
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
