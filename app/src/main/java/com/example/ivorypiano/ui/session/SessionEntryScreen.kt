package com.example.ivorypiano.ui.session

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ivorypiano.IvoryPianoTopAppBar
import com.example.ivorypiano.R
import com.example.ivorypiano.ui.AppViewModelProvider
import com.example.ivorypiano.ui.DevicePreviews
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
    SessionEntryScreenContent(
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
        canNavigateBack = canNavigateBack,
        onNavigateUp = onNavigateUp
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SessionEntryScreenContent(
    sessionUiState: SessionUiState,
    onSessionValueChange: (SessionDetails) -> Unit,
    onStartTimer: () -> Unit,
    onPauseTimer: () -> Unit,
    onResetTimer: () -> Unit,
    onSaveClick: () -> Unit,
    canNavigateBack: Boolean,
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {
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
            sessionUiState = sessionUiState,
            onSessionValueChange = onSessionValueChange,
            onStartTimer = onStartTimer,
            onPauseTimer = onPauseTimer,
            onResetTimer = onResetTimer,
            onSaveClick = onSaveClick,
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
    modifier: Modifier = Modifier,
    isTimerVisible: Boolean = true
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(32.dp),
        modifier = modifier.padding(24.dp)
    ) {
        if (isTimerVisible) {
            TimerSection(
                timerMillis = sessionUiState.timerMillis,
                isRunning = sessionUiState.isTimerRunning,
                onStart = onStartTimer,
                onPause = onPauseTimer,
                onReset = onResetTimer,
                modifier = Modifier.fillMaxWidth()
            )
        } else {
            DurationManualInput(
                durationMillis = sessionUiState.sessionDetails.durationMillis,
                onDurationChange = { newMillis ->
                    onSessionValueChange(sessionUiState.sessionDetails.copy(durationMillis = newMillis))
                },
                modifier = Modifier.fillMaxWidth()
            )
        }

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
fun DurationManualInput(
    durationMillis: Long,
    onDurationChange: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    // Displaying the duration in total minutes for manual editing
    var textValue by remember(durationMillis) {
        mutableStateOf((durationMillis / 60000).toString())
    }

    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "Edit Duration",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.outline,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        OutlinedTextField(
            value = textValue,
            onValueChange = { newValue ->
                if (newValue.all { it.isDigit() }) {
                    textValue = newValue
                    val minutes = newValue.toLongOrNull() ?: 0L
                    onDurationChange(minutes * 60000)
                }
            },
            label = { Text("Duration (minutes)") },
            suffix = { Text("min") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant
            )
        )
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

@DevicePreviews
@Composable
fun SessionEntryPreview() {
    val uiState = SessionUiState(
        sessionDetails = SessionDetails(
            pieceName = "Moonlight Sonata",
            composer = "Beethoven",
            bpm = "60",
            measures = "100"
        ),
        isEntryValid = true
    )

    IvoryPianoTheme {
        SessionEntryScreenContent(
            sessionUiState = uiState,
            onSessionValueChange = { },
            onStartTimer = { },
            onPauseTimer = { },
            onResetTimer = { },
            onSaveClick = {},
            canNavigateBack = true,
            onNavigateUp = {}
        )
    }
}
