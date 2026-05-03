package com.example.ivorypiano.ui.session

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ivorypiano.IvoryPianoTopAppBar
import com.example.ivorypiano.R
import com.example.ivorypiano.data.DataSource
import com.example.ivorypiano.data.PianoSession
import com.example.ivorypiano.ui.AppViewModelProvider
import com.example.ivorypiano.ui.DevicePreviews
import com.example.ivorypiano.ui.theme.IvoryPianoTheme
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Entry route for home Screen
 */
@Composable
fun HomeScreen(
    navigateToSessionEntry: () -> Unit,
    navigateToSessionUpdate: (Int) -> Unit,
    navigateToProfile: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val homeUiState by viewModel.homeUiState.collectAsState()
    var isSearchActive by remember { mutableStateOf(false) }

    HomeScreenContent(
        sessionList = homeUiState.sessionList,
        aggregatedData = homeUiState.aggregatedData,
        aggregationType = homeUiState.aggregationType,
        searchQuery = homeUiState.searchQuery,
        isSearchActive = isSearchActive,
        onSearchQueryChange = viewModel::updateSearchQuery,
        onToggleSearch = {
            isSearchActive = !isSearchActive
            if (!isSearchActive) viewModel.updateSearchQuery("")
        },
        onAggregationChange = viewModel::setAggregationType,
        onSessionClick = navigateToSessionUpdate,
        onAddSessionClick = navigateToSessionEntry,
        onProfileClick = navigateToProfile,
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeScreenContent(
    sessionList: List<PianoSession>,
    aggregatedData: Map<String, List<PianoSession>>,
    aggregationType: AggregationType,
    searchQuery: String,
    isSearchActive: Boolean,
    onSearchQueryChange: (String) -> Unit,
    onToggleSearch: () -> Unit,
    onAggregationChange: (AggregationType) -> Unit,
    onSessionClick: (Int) -> Unit,
    onAddSessionClick: () -> Unit,
    onProfileClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    var showFilterMenu by remember { mutableStateOf(false) }

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            IvoryPianoTopAppBar(
                title = if (isSearchActive) "" else stringResource(HomeDestination.titleRes),
                canNavigateBack = false,
                scrollBehavior = scrollBehavior,
                actions = {
                    if (isSearchActive) {
                        TextField(
                            value = searchQuery,
                            onValueChange = onSearchQueryChange,
                            placeholder = { Text("Search piece or composer...") },
                            modifier = Modifier
                                .weight(1f)
                                .padding(horizontal = 8.dp),
                            singleLine = true,
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = MaterialTheme.colorScheme.surface,
                                unfocusedContainerColor = MaterialTheme.colorScheme.surface
                            ),
                            trailingIcon = {
                                IconButton(onClick = onToggleSearch) {
                                    Icon(Icons.Default.Clear, contentDescription = "Close Search")
                                }
                            }
                        )
                    } else {
                        IconButton(onClick = onToggleSearch) {
                            Icon(Icons.Default.Search, contentDescription = "Search")
                        }
                        Box {
                            IconButton(onClick = { showFilterMenu = true }) {
                                Icon(Icons.Default.FilterList, contentDescription = "Filter")
                            }
                            DropdownMenu(
                                expanded = showFilterMenu,
                                onDismissRequest = { showFilterMenu = false }
                            ) {
                                AggregationType.entries.forEach { type ->
                                    DropdownMenuItem(
                                        text = {
                                            Text(
                                                type.name.lowercase()
                                                    .replaceFirstChar { it.uppercase() })
                                        },
                                        onClick = {
                                            onAggregationChange(type)
                                            showFilterMenu = false
                                        },
                                        leadingIcon = {
                                            RadioButton(
                                                selected = aggregationType == type,
                                                onClick = null
                                            )
                                        }
                                    )
                                }
                            }
                        }
                        IconButton(onClick = onProfileClick) {
                            Icon(
                                imageVector = Icons.Default.AccountCircle,
                                contentDescription = "Profile"
                            )
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddSessionClick,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                shape = MaterialTheme.shapes.extraLarge,
                modifier = Modifier.padding(20.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Session"
                )
            }
        },
    ) { innerPadding ->
        HomeBody(
            sessionList = sessionList,
            aggregatedData = aggregatedData,
            aggregationType = aggregationType,
            onSessionClick = onSessionClick,
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@Composable
private fun HomeBody(
    sessionList: List<PianoSession>,
    aggregatedData: Map<String, List<PianoSession>>,
    aggregationType: AggregationType,
    onSessionClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxSize()
    ) {
        if (sessionList.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    modifier = Modifier.padding(horizontal = 32.dp),
                    text = stringResource(R.string.no_sessions),
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.outline,
                    fontStyle = FontStyle.Italic
                )
            }
        } else {
            if (aggregationType == AggregationType.NONE) {
                SessionList(
                    sessionList = sessionList,
                    onSessionClick = { onSessionClick(it.id) },
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            } else {
                AggregatedSessionList(
                    aggregatedData = aggregatedData,
                    onSessionClick = { onSessionClick(it.id) },
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
        }
    }
}

@Composable
private fun AggregatedSessionList(
    aggregatedData: Map<String, List<PianoSession>>,
    onSessionClick: (PianoSession) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        aggregatedData.forEach { (header, sessions) ->
            if (sessions.isNotEmpty()) {
                item {
                    Text(
                        text = header.ifBlank { "Unknown" },
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }
                items(items = sessions, key = { it.id }) { session ->
                    SessionItem(
                        session = session,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onSessionClick(session) }
                    )
                    HorizontalDivider(
                        modifier = Modifier.padding(top = 16.dp),
                        thickness = 0.5.dp,
                        color = MaterialTheme.colorScheme.outlineVariant
                    )
                }
            }
        }
    }
}

@Composable
private fun SessionList(
    sessionList: List<PianoSession>,
    onSessionClick: (PianoSession) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(items = sessionList, key = { it.id }) { session ->
            SessionItem(
                session = session,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onSessionClick(session) }
            )
            HorizontalDivider(
                modifier = Modifier.padding(top = 16.dp),
                thickness = 0.5.dp,
                color = MaterialTheme.colorScheme.outlineVariant
            )
        }
    }
}

@Composable
private fun SessionItem(
    session: PianoSession,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top
        ) {
            Text(
                text = if (session.pieceName.isBlank()) stringResource(R.string.untitled_composition) else session.pieceName,
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.weight(1f)
            )
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = session.date,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.secondary
                )
                Text(
                    text = formatTime(session.timestamp),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.outline
                )
            }
        }

        if (session.composer.isNotBlank()) {
            Text(
                text = stringResource(R.string.by_composer, session.composer),
                style = MaterialTheme.typography.titleMedium,
                fontStyle = FontStyle.Italic,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Row(
            modifier = Modifier.padding(top = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.duration),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.outline
            )
            Text(
                text = " — ${formatDuration(session.durationMillis)}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

private fun formatDuration(millis: Long): String {
    val seconds = (millis / 1000) % 60
    val minutes = (millis / (1000 * 60)) % 60
    val hours = (millis / (1000 * 60 * 60))
    return if (hours > 0) {
        String.format("%dh %02dm %02ds", hours, minutes, seconds)
    } else {
        String.format("%02dm %02ds", minutes, seconds)
    }
}

private fun formatTime(timestamp: Long): String {
    return if (timestamp == 0L) ""
    else SimpleDateFormat("h:mm a", Locale.getDefault()).format(Date(timestamp))
}

@DevicePreviews
@Composable
fun HomePreview() {
    IvoryPianoTheme {
        Surface(
            color = MaterialTheme.colorScheme.background
        ) {
            HomeScreenContent(
                sessionList = DataSource.dummySessions,
                aggregatedData = emptyMap(),
                aggregationType = AggregationType.NONE,
                searchQuery = "",
                isSearchActive = false,
                onSearchQueryChange = {},
                onToggleSearch = {},
                onAggregationChange = {},
                onSessionClick = {},
                onAddSessionClick = {},
                onProfileClick = {}
            )
        }
    }
}
