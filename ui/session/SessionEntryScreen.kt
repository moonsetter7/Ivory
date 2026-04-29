package com.example.ivorypiano.ui.session

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ivorypiano.ui.AppViewModelProvider
import kotlinx.coroutines.launch
import java.util.Date

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
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier.padding(16.dp)
    ) {
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
        // Simplified Date Input for context
        OutlinedTextField(
            value = sessionDetails.date,
            onValueChange = { onValueChange(sessionDetails.copy(date = it)) },
            label = { Text("Date (YYYY-MM-DD)*") },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )
    }
}
