package com.example.ivorypiano.ui.session

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ivorypiano.IvoryPianoTopAppBar
import com.example.ivorypiano.R
import com.example.ivorypiano.ui.AppViewModelProvider
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SessionDetailsScreen(
    navigateToEditSession: (Int) -> Unit,
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SessionDetailsViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val uiState = viewModel.uiState.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            IvoryPianoTopAppBar(
                title = stringResource(R.string.session_details_title),
                canNavigateBack = true,
                navigateUp = navigateBack
            )
        },
        floatingActionButton = {
            var deleteConfirmationRequired by rememberSaveable { mutableStateOf(false) }
            FloatingActionButton(
                onClick = { deleteConfirmationRequired = true },
                containerColor = MaterialTheme.colorScheme.errorContainer,
                contentColor = MaterialTheme.colorScheme.onErrorContainer,
                shape = MaterialTheme.shapes.extraLarge,
                modifier = Modifier.padding(20.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = stringResource(R.string.delete)
                )
            }
            if (deleteConfirmationRequired) {
                DeleteConfirmationDialog(
                    onDeleteConfirm = {
                        deleteConfirmationRequired = false
                        coroutineScope.launch {
                            viewModel.deleteSession()
                            navigateBack()
                        }
                    },
                    onDeleteCancel = { deleteConfirmationRequired = false },
                    modifier = Modifier.padding(16.dp)
                )
            }
        },
    ) { innerPadding ->
        SessionDetailsBody(
            sessionUiState = uiState.value,
            modifier = modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .fillMaxSize()
        )
    }
}

@Composable
private fun SessionDetailsBody(
    sessionUiState: SessionUiState,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // Header Section
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = if (sessionUiState.sessionDetails.pieceName.isNullOrBlank()) {
                    stringResource(R.string.untitled_composition)
                } else {
                    sessionUiState.sessionDetails.pieceName
                },
                style = MaterialTheme.typography.displaySmall,
                color = MaterialTheme.colorScheme.primary
            )
            if (!sessionUiState.sessionDetails.composer.isNullOrBlank()) {
                Text(
                    text = stringResource(R.string.by_composer, sessionUiState.sessionDetails.composer),
                    style = MaterialTheme.typography.headlineSmall,
                    fontStyle = FontStyle.Italic,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
        }

        HorizontalDivider(thickness = 0.5.dp, color = MaterialTheme.colorScheme.outlineVariant)

        // Details Grid/List
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            DetailItem(
                label = stringResource(R.string.duration),
                value = formatDuration(sessionUiState.sessionDetails.durationMillis),
                isHighlight = true
            )
            DetailItem(
                label = stringResource(R.string.bpm),
                value = if (sessionUiState.sessionDetails.bpm.isNullOrBlank()) {
                    stringResource(R.string.empty_value)
                } else {
                    stringResource(R.string.bpm_format, sessionUiState.sessionDetails.bpm)
                }
            )
            DetailItem(
                label = stringResource(R.string.measures),
                value = if (sessionUiState.sessionDetails.measures.isNullOrBlank()) {
                    stringResource(R.string.empty_value)
                } else {
                    stringResource(R.string.measures_format, sessionUiState.sessionDetails.measures)
                }
            )
            DetailItem(
                label = stringResource(R.string.date),
                value = sessionUiState.sessionDetails.date
            )
            DetailItem(
                label = stringResource(R.string.timestamp),
                value = formatTimestamp(sessionUiState.sessionDetails.timestamp)
            )
        }
    }
}

@Composable
private fun DetailItem(
    label: String,
    value: String,
    isHighlight: Boolean = false
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.outline
        )
        Text(
            text = value,
            style = if (isHighlight) MaterialTheme.typography.headlineSmall else MaterialTheme.typography.bodyLarge,
            color = if (isHighlight) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
            fontWeight = if (isHighlight) FontWeight.Bold else FontWeight.Normal
        )
    }
}

@Composable
private fun DeleteConfirmationDialog(
    onDeleteConfirm: () -> Unit,
    onDeleteCancel: () -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        onDismissRequest = { onDeleteCancel() },
        title = { 
            Text(
                stringResource(R.string.delete), 
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.error
            ) 
        },
        text = { 
            Text(
                stringResource(R.string.delete_question),
                style = MaterialTheme.typography.bodyMedium
            ) 
        },
        modifier = modifier,
        dismissButton = {
            TextButton(onClick = onDeleteCancel) {
                Text(stringResource(R.string.no), color = MaterialTheme.colorScheme.outline)
            }
        },
        confirmButton = {
            TextButton(onClick = onDeleteConfirm) {
                Text(stringResource(R.string.yes), color = MaterialTheme.colorScheme.error)
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

private fun formatTimestamp(timestamp: Long): String {
    return if (timestamp == 0L) "—"
    else SimpleDateFormat("h:mm a", Locale.getDefault()).format(Date(timestamp))
}
