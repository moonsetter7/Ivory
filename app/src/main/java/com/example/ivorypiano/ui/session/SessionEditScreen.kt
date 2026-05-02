package com.example.ivorypiano.ui.session

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ivorypiano.IvoryPianoTopAppBar
import com.example.ivorypiano.R
import com.example.ivorypiano.ui.AppViewModelProvider
import com.example.ivorypiano.ui.navigation.NavigationDestination
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SessionEditScreen(
    navigateBack: () -> Unit,
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SessionEditViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val coroutineScope = rememberCoroutineScope()
    Scaffold(
        topBar = {
            IvoryPianoTopAppBar(
                title = stringResource(SessionEditDestination.titleRes),
                canNavigateBack = true,
                navigateUp = onNavigateUp
            )
        },
        modifier = modifier
    ) { innerPadding ->
        SessionEntryBody(
            sessionUiState = viewModel.sessionUiState,
            onSessionValueChange = viewModel::updateUiState,
            onSaveClick = {
                // Note: If the user rotates the screen very fast, the operation may get cancelled
                // and the item may not be updated in the Database. This is because when config
                // change occurs, the Activity will be recreated and the rememberCoroutineScope will
                // be cancelled - since the scope is bound to composition.

                // ... Although, I haven't tested it out yet. The Inventory app left the above
                // comment within a file of similar function (ItemEditScreen), so I'm just leaving
                // this warning here too.
                coroutineScope.launch {
                    viewModel.updateSession()
                    navigateBack()
                }
            },
            onStartTimer = {}, // Timer not used in edit mode
            onPauseTimer = {},
            onResetTimer = {},
            modifier = Modifier.padding(innerPadding),
            isTimerVisible = false
        )
    }
}
