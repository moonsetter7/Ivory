package com.example.ivorypiano.ui.user

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.AvTimer
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Timeline
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ivorypiano.IvoryPianoTopAppBar
import com.example.ivorypiano.R
import com.example.ivorypiano.data.DataSource
import com.example.ivorypiano.data.User
import com.example.ivorypiano.ui.AppViewModelProvider
import com.example.ivorypiano.ui.DevicePreviews
import com.example.ivorypiano.ui.navigation.NavigationDestination
import com.example.ivorypiano.ui.theme.IvoryPianoTheme

object ProfileDestination : NavigationDestination {
    override val route = "profile"
    override val titleRes = R.string.profile_title
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onSignOut: () -> Unit,
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ProfileViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val uiState by viewModel.uiState.collectAsState()
    var showSignOutDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            IvoryPianoTopAppBar(
                title = stringResource(ProfileDestination.titleRes),
                canNavigateBack = true,
                navigateUp = navigateBack
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showSignOutDialog = true },
                containerColor = MaterialTheme.colorScheme.errorContainer,
                contentColor = MaterialTheme.colorScheme.onErrorContainer,
                shape = MaterialTheme.shapes.extraLarge,
                modifier = Modifier.padding(20.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Logout,
                    contentDescription = stringResource(R.string.sign_out)
                )
            }
        }
    ) { innerPadding ->
        ProfileBody(
            uiState = uiState,
            modifier = modifier.padding(innerPadding)
        )

        if (showSignOutDialog) {
            SignOutConfirmationDialog(
                onConfirm = {
                    showSignOutDialog = false
                    viewModel.signOut()
                    onSignOut()
                },
                onDismiss = { showSignOutDialog = false }
            )
        }
    }
}

@Composable
fun ProfileBody(
    uiState: ProfileUiState,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {
        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Personal Information",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.secondary,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        ProfileInfoItem(
            label = stringResource(R.string.username_label),
            value = uiState.user?.username ?: "",
            icon = Icons.Default.Person
        )

        HorizontalDivider(
            thickness = 0.5.dp,
            color = MaterialTheme.colorScheme.outlineVariant
        )

        ProfileInfoItem(
            label = stringResource(R.string.email_label),
            value = uiState.user?.email ?: "",
            icon = Icons.Default.Email
        )

        HorizontalDivider(
            thickness = 0.5.dp,
            color = MaterialTheme.colorScheme.outlineVariant
        )

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = stringResource(R.string.statistics),
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.secondary,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        ProfileInfoItem(
            label = stringResource(R.string.total_sessions),
            value = uiState.totalSessions.toString(),
            icon = Icons.Default.History
        )

        HorizontalDivider(
            thickness = 0.5.dp,
            color = MaterialTheme.colorScheme.outlineVariant
        )

        ProfileInfoItem(
            label = stringResource(R.string.total_practice_time),
            value = formatDuration(uiState.totalDurationMillis),
            icon = Icons.Default.Timeline
        )

        HorizontalDivider(
            thickness = 0.5.dp,
            color = MaterialTheme.colorScheme.outlineVariant
        )

        ProfileInfoItem(
            label = stringResource(R.string.average_session_time),
            value = formatDuration(uiState.averageDurationMillis),
            icon = Icons.Default.AvTimer
        )

        HorizontalDivider(
            thickness = 0.5.dp,
            color = MaterialTheme.colorScheme.outlineVariant
        )
    }
}

@Composable
fun ProfileInfoItem(
    label: String,
    value: String,
    icon: ImageVector,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(28.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.outline
            )
            Text(
                text = value,
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
private fun SignOutConfirmationDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = stringResource(R.string.sign_out),
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.error
            )
        },
        text = {
            Text(
                text = stringResource(R.string.sign_out_question),
                style = MaterialTheme.typography.bodyMedium
            )
        },
        modifier = modifier,
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = stringResource(R.string.no), color = MaterialTheme.colorScheme.outline)
            }
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(
                    text = stringResource(R.string.sign_out_confirm),
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    )
}

@Composable
private fun formatDuration(millis: Long): String {
    val seconds = (millis / 1000) % 60
    val minutes = (millis / (1000 * 60)) % 60
    val hours = (millis / (1000 * 60 * 60))
    return if (hours > 0) {
        stringResource(R.string.duration_h_m_s, hours, minutes, seconds)
    } else {
        stringResource(R.string.duration_m_s, minutes, seconds)
    }
}

@DevicePreviews
@Composable
fun ProfileScreenPreview() {
    val dummySessions = DataSource.dummySessions
    val totalSessions = dummySessions.size
    val totalDuration = dummySessions.sumOf { it.durationMillis }
    val avgDuration = if (totalSessions > 0) totalDuration / totalSessions else 0L

    IvoryPianoTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            Scaffold(
                floatingActionButton = {
                    FloatingActionButton(
                        onClick = {},
                        containerColor = MaterialTheme.colorScheme.errorContainer,
                        contentColor = MaterialTheme.colorScheme.onErrorContainer,
                        shape = MaterialTheme.shapes.extraLarge,
                        modifier = Modifier.padding(20.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Logout,
                            contentDescription = stringResource(R.string.sign_out)
                        )
                    }
                }
            ) { innerPadding ->
                ProfileBody(
                    uiState = ProfileUiState(
                        user = User(
                            username = "rachmaninoff",
                            email = "concerto@secondmovement.com",
                            passwordHash = ""
                        ),
                        totalSessions = totalSessions,
                        totalDurationMillis = totalDuration,
                        averageDurationMillis = avgDuration
                    ),
                    modifier = Modifier.padding(innerPadding)
                )
            }
        }
    }
}
