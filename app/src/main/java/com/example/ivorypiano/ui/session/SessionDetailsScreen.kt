package com.example.ivorypiano.ui.session

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
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
                title = stringResource(SessionDetailsDestination.titleRes),
                canNavigateBack = true,
                navigateUp = navigateBack
            )
        },
        floatingActionButton = {
            var deleteConfirmationRequired by rememberSaveable { mutableStateOf(false) }
            FloatingActionButton(
                onClick = { deleteConfirmationRequired = true },
                shape = MaterialTheme.shapes.medium,
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
        )
    }
}

@Composable
private fun SessionDetailsBody(
    sessionUiState: SessionUiState,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                SessionDetailsRow(
                    labelResID = R.string.piece_name,
                    sessionDetail = if (sessionUiState.sessionDetails.pieceName.isNullOrBlank()) "No Piece" else sessionUiState.sessionDetails.pieceName,
                )
                SessionDetailsRow(
                    labelResID = R.string.composer,
                    sessionDetail = if (sessionUiState.sessionDetails.composer.isNullOrBlank()) "N/A" else sessionUiState.sessionDetails.composer,
                )
                SessionDetailsRow(
                    labelResID = R.string.bpm,
                    sessionDetail = if (sessionUiState.sessionDetails.bpm.isNullOrBlank()) "N/A" else sessionUiState.sessionDetails.bpm,
                )
                SessionDetailsRow(
                    labelResID = R.string.measures,
                    sessionDetail = if (sessionUiState.sessionDetails.measures.isNullOrBlank()) "N/A" else sessionUiState.sessionDetails.measures,
                )
                SessionDetailsRow(
                    labelResID = R.string.duration,
                    sessionDetail = formatDuration(sessionUiState.sessionDetails.durationMillis),
                )
                SessionDetailsRow(
                    labelResID = R.string.date,
                    sessionDetail = sessionUiState.sessionDetails.date,
                )
                SessionDetailsRow(
                    labelResID = R.string.timestamp,
                    sessionDetail = formatTimestamp(sessionUiState.sessionDetails.timestamp),
                )
            }
        }
    }
}

@Composable
private fun SessionDetailsRow(
    @StringRes labelResID: Int, sessionDetail: String, modifier: Modifier = Modifier
) {
    Row(modifier = modifier) {
        Text(text = stringResource(labelResID), style = MaterialTheme.typography.labelLarge)
        Spacer(modifier = Modifier.weight(1f))
        Text(text = sessionDetail, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyLarge)
    }
}

@Composable
private fun DeleteConfirmationDialog(
    onDeleteConfirm: () -> Unit,
    onDeleteCancel: () -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(onDismissRequest = { /* Do nothing */ },
        title = { Text(stringResource(R.string.delete)) },
        text = { Text(stringResource(R.string.delete_question)) },
        modifier = modifier,
        dismissButton = {
            TextButton(onClick = onDeleteCancel) {
                Text(text = stringResource(R.string.no))
            }
        },
        confirmButton = {
            TextButton(onClick = onDeleteConfirm) {
                Text(text = stringResource(R.string.yes))
            }
        })
}

private fun formatDuration(millis: Long): String {
    val seconds = (millis / 1000) % 60
    val minutes = (millis / (1000 * 60)) % 60
    val hours = (millis / (1000 * 60 * 60))
    return String.format("%02d:%02d:%02d", hours, minutes, seconds)
}

private fun formatTimestamp(timestamp: Long): String {
    return if (timestamp == 0L) "N/A"
    else SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date(timestamp))
}
