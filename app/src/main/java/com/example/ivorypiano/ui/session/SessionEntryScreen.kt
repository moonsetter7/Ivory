package com.example.ivorypiano.ui.session

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ivorypiano.ui.AppViewModelProvider
import com.example.ivorypiano.ui.theme.IvoryPianoTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SessionEntryScreen(
    navigateBack: () -> Unit,
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    canNavigateBack: Boolean = true,
    viewModel: SessionEntryViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val coroutineScope = rememberCoroutineScope()
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Log Practice Session") }
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
            modifier = modifier
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
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier.padding(16.dp)
    ) {
        TimerSection(
            timerMillis = sessionUiState.timerMillis,
            isRunning = sessionUiState.isTimerRunning,
            onStart = onStartTimer,
            onPause = onPauseTimer,
            onReset = onResetTimer,
            modifier = Modifier.fillMaxWidth()
        )

        SessionInputForm(
            sessionDetails = sessionUiState.sessionDetails,
            onValueChange = onSessionValueChange,
            modifier = Modifier.fillMaxWidth()
        )
        
        Button(
            onClick = onSaveClick,
            enabled = sessionUiState.isEntryValid,
            shape = MaterialTheme.shapes.small,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save Session")
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

    Card(modifier = modifier) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = timeDisplay,
                style = MaterialTheme.typography.displayMedium
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(top = 8.dp)
            ) {
                if (!isRunning) {
                    Button(onClick = onStart) { Text("Start") }
                } else {
                    Button(onClick = onPause) { Text("Pause") }
                }
                OutlinedButton(onClick = onReset) { Text("Reset") }
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
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        OutlinedTextField(
            value = sessionDetails.pieceName ?: "",
            onValueChange = { onValueChange(sessionDetails.copy(pieceName = it)) },
            label = { Text("Piece Name*") },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )
        OutlinedTextField(
            value = sessionDetails.composer ?: "",
            onValueChange = { onValueChange(sessionDetails.copy(composer = it)) },
            label = { Text("Composer") },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedTextField(
                value = sessionDetails.bpm ?: "",
                onValueChange = { onValueChange(sessionDetails.copy(bpm = it)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                label = { Text("BPM") },
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
