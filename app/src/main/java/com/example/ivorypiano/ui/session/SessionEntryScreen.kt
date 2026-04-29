package com.example.ivorypiano.ui.session

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ivorypiano.IvoryPianoTopAppBar
import com.example.ivorypiano.R
import com.example.ivorypiano.ui.AppViewModelProvider
import com.example.ivorypiano.ui.theme.IvoryPianoTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SessionEntryScreen(
    navigateBack: () -> Unit,
    onNavigateUp: () -> Unit,
    canNavigateBack: Boolean = true,
    viewModel: SessionEntryViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val coroutineScope = rememberCoroutineScope()
    Scaffold(
        topBar = {
            IvoryPianoTopAppBar(
                title = "New Practice Entry",
                canNavigateBack = canNavigateBack,
                navigateUp = onNavigateUp
            )
        }
    ) { innerPadding ->
        SessionEntryBody(
            sessionUiState = viewModel.sessionUiState,
            onSessionValueChange = viewModel::updateUiState,
            onStartTimer = viewModel::startTimer,
            onPauseTimer = viewModel::pauseTimer,
            onResetTimer = viewModel::resetTimer,
            onSaveClick = {
                coroutineScope.launch {
                    viewModel.saveSession()
                    navigateBack()
                }
            },
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .fillMaxWidth()
        )
    }
}

@Composable
fun SessionEntryBody(
    sessionUiState: SessionUiState,
    onSessionValueChange: (SessionDetails) -> Unit,
    onStartTimer: () -> Unit,
    onPauseTimer: () -> Unit,
    onResetTimer: () -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(32.dp),
        modifier = modifier.padding(24.dp)
    ) {
        TimerSection(
            timerMillis = sessionUiState.timerMillis,
            isRunning = sessionUiState.isTimerRunning,
            onStart = onStartTimer,
            onPause = onPauseTimer,
            onReset = onResetTimer,
            modifier = Modifier.fillMaxWidth()
        )

        HorizontalDivider(thickness = 0.5.dp, color = MaterialTheme.colorScheme.outlineVariant)

        SessionInputForm(
            sessionDetails = sessionUiState.sessionDetails,
            onValueChange = onSessionValueChange,
            modifier = Modifier.fillMaxWidth()
        )
        
        Button(
            onClick = onSaveClick,
            enabled = sessionUiState.isEntryValid,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ),
            shape = MaterialTheme.shapes.extraLarge,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            Text(
                stringResource(R.string.save),
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}

@Composable
fun TimerSection(
    timerMillis: Long,
    isRunning: Boolean,
    onStart: () -> Unit,
    onPause: () -> Unit,
    onReset: () -> Unit,
    modifier: Modifier = Modifier
) {
    val seconds = (timerMillis / 1000) % 60
    val minutes = (timerMillis / (1000 * 60)) % 60
    val hours = (timerMillis / (1000 * 60 * 60))
    val timeDisplay = String.format("%02d:%02d:%02d", hours, minutes, seconds)

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Practice Duration",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.outline
        )
        Text(
            text = timeDisplay,
            style = MaterialTheme.typography.displayLarge,
            color = MaterialTheme.colorScheme.primary
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(top = 16.dp)
        ) {
            if (!isRunning) {
                Button(
                    onClick = onStart,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer,
                        contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                ) { 
                    Text("Start Timer") 
                }
            } else {
                OutlinedButton(onClick = onPause) { 
                    Text("Pause") 
                }
            }
            TextButton(
                onClick = onReset,
                colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.outline)
            ) { 
                Text("Reset") 
            }
        }
    }
}

@Composable
fun SessionInputForm(
    sessionDetails: SessionDetails,
    onValueChange: (SessionDetails) -> Unit = {},
    enabled: Boolean = true,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                text = "Composition Details",
                style = MaterialTheme.typography.titleMedium,
                fontStyle = FontStyle.Italic,
                color = MaterialTheme.colorScheme.secondary
            )
            
            OutlinedTextField(
                value = sessionDetails.pieceName ?: "",
                onValueChange = { onValueChange(sessionDetails.copy(pieceName = it)) },
                label = { Text("Title of the Piece") },
                modifier = Modifier.fillMaxWidth(),
                enabled = enabled,
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant
                )
            )
            
            OutlinedTextField(
                value = sessionDetails.composer ?: "",
                onValueChange = { onValueChange(sessionDetails.copy(composer = it)) },
                label = { Text(stringResource(R.string.composer)) },
                modifier = Modifier.fillMaxWidth(),
                enabled = enabled,
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant
                )
            )
        }

        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            OutlinedTextField(
                value = sessionDetails.bpm ?: "",
                onValueChange = { onValueChange(sessionDetails.copy(bpm = it)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                label = { Text("Tempo (BPM)") },
                modifier = Modifier.weight(1f),
                enabled = enabled,
                singleLine = true
            )
            OutlinedTextField(
                value = sessionDetails.measures ?: "",
                onValueChange = { onValueChange(sessionDetails.copy(measures = it)) },
                label = { Text("Measures") },
                modifier = Modifier.weight(1f),
                enabled = enabled,
                singleLine = true
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SessionEntryScreenPreview() {
    var uiState by remember { mutableStateOf(SessionUiState(isEntryValid = true)) }

    IvoryPianoTheme {
        SessionEntryBody(
            sessionUiState = uiState,
            onSessionValueChange = { uiState = uiState.copy(sessionDetails = it) },
            onStartTimer = { uiState = uiState.copy(isTimerRunning = true) },
            onPauseTimer = { uiState = uiState.copy(isTimerRunning = false) },
            onResetTimer = { uiState = uiState.copy(timerMillis = 0, isTimerRunning = false) },
            onSaveClick = {}
        )
    }
}
