package com.app.kind.kind.prime.kindprimeapp.ui.events

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.app.kind.kind.prime.kindprimeapp.R
import com.app.kind.kind.prime.kindprimeapp.data.EventEntity
import com.app.kind.kind.prime.kindprimeapp.ui.theme.KindPrimeAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: EventViewModel = viewModel(),
) {
    val events by viewModel.events.collectAsStateWithLifecycle()
    var showForm by rememberSaveable { mutableStateOf(false) }
    var eventToEdit by remember { mutableStateOf<EventEntity?>(null) }
    var eventToDelete by remember { mutableStateOf<EventEntity?>(null) }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        // El Scaffold de MainActivity ya aplica los insets del sistema.
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.home_title)) },
                windowInsets = WindowInsets(0, 0, 0, 0),
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainer,
                ),
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = {
                    eventToEdit = null
                    showForm = true
                },
                icon = { Icon(Icons.Filled.Add, contentDescription = null) },
                text = { Text(stringResource(R.string.add_event)) },
            )
        },
    ) { innerPadding ->
        if (events.isEmpty()) {
            EmptyEventsState(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentPadding = PaddingValues(
                    start = 16.dp,
                    top = 12.dp,
                    end = 16.dp,
                    bottom = 96.dp,
                ),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                items(events, key = { it.id }) { event ->
                    EventCard(
                        event = event,
                        onToggleCompleted = { viewModel.toggleCompleted(event) },
                        onEdit = {
                            eventToEdit = event
                            showForm = true
                        },
                        onDelete = { eventToDelete = event },
                    )
                }
            }
        }
    }

    if (showForm) {
        EventFormDialog(
            initialEvent = eventToEdit,
            onDismiss = {
                showForm = false
                eventToEdit = null
            },
            onSave = { title, description, eventDate, completed ->
                viewModel.saveEvent(
                    id = eventToEdit?.id ?: 0L,
                    title = title,
                    description = description,
                    eventDate = eventDate,
                    completed = completed,
                )
                showForm = false
                eventToEdit = null
            },
        )
    }

    eventToDelete?.let { event ->
        DeleteEventDialog(
            event = event,
            onDismiss = { eventToDelete = null },
            onConfirm = {
                viewModel.deleteEvent(event)
                eventToDelete = null
            },
        )
    }
}

@Composable
private fun EmptyEventsState(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(
            imageVector = Icons.Filled.DateRange,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colorScheme.primary,
        )
        Spacer(Modifier.height(16.dp))
        Text(
            text = stringResource(R.string.empty_events_title),
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center,
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = stringResource(R.string.empty_events_subtitle),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
private fun EventCard(
    event: EventEntity,
    onToggleCompleted: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val days = daysUntil(event.eventDate)

    val remainingLabel = when {
        event.completed -> stringResource(R.string.status_completed)
        days < 0 -> pluralStringResource(R.plurals.status_overdue, (-days).toInt(), (-days).toInt())
        days == 0L -> stringResource(R.string.status_today)
        else -> pluralStringResource(R.plurals.status_remaining, days.toInt(), days.toInt())
    }

    val badgeColor = when {
        event.completed -> MaterialTheme.colorScheme.primary
        days < 0 -> MaterialTheme.colorScheme.error
        days == 0L -> MaterialTheme.colorScheme.tertiary
        else -> MaterialTheme.colorScheme.primary.copy(alpha = 0.14f)
    }
    val badgeContentColor = when {
        event.completed -> MaterialTheme.colorScheme.onPrimary
        days < 0 -> MaterialTheme.colorScheme.onError
        days == 0L -> MaterialTheme.colorScheme.onTertiary
        else -> MaterialTheme.colorScheme.primary
    }

    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer,
        ),
    ) {
        Column(
            modifier = Modifier.padding(start = 8.dp, top = 8.dp, end = 4.dp, bottom = 12.dp),
        ) {
            Row(verticalAlignment = Alignment.Top) {
                Checkbox(
                    checked = event.completed,
                    onCheckedChange = { onToggleCompleted() },
                    colors = CheckboxDefaults.colors(
                        checkedColor = MaterialTheme.colorScheme.primary,
                        checkmarkColor = MaterialTheme.colorScheme.onPrimary,
                        uncheckedColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    ),
                )
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(top = 12.dp),
                ) {
                    Text(
                        text = event.title,
                        style = MaterialTheme.typography.titleMedium,
                        textDecoration = if (event.completed) TextDecoration.LineThrough else null,
                        color = if (event.completed) {
                            MaterialTheme.colorScheme.onSurfaceVariant
                        } else {
                            MaterialTheme.colorScheme.onSurface
                        },
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                    )
                    if (event.description.isNotBlank()) {
                        Spacer(Modifier.height(4.dp))
                        Text(
                            text = event.description,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                }
                IconButton(onClick = onEdit) {
                    Icon(
                        imageVector = Icons.Filled.Edit,
                        contentDescription = stringResource(R.string.edit),
                        tint = MaterialTheme.colorScheme.primary,
                    )
                }
                IconButton(onClick = onDelete) {
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = stringResource(R.string.delete),
                        tint = MaterialTheme.colorScheme.error,
                    )
                }
            }
            Row(
                modifier = Modifier.padding(start = 48.dp, end = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    imageVector = Icons.Filled.DateRange,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Spacer(Modifier.width(6.dp))
                Text(
                    text = formatEventDate(event.eventDate),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Spacer(Modifier.weight(1f))
                Surface(
                    color = badgeColor,
                    contentColor = badgeContentColor,
                    shape = MaterialTheme.shapes.small,
                ) {
                    Text(
                        text = remainingLabel,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                        style = MaterialTheme.typography.labelSmall,
                    )
                }
            }
        }
    }
}

@Composable
private fun DeleteEventDialog(
    event: EventEntity,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.delete_event_title)) },
        text = { Text(stringResource(R.string.delete_event_message, event.title)) },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(
                    text = stringResource(R.string.delete),
                    color = MaterialTheme.colorScheme.error,
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.cancel))
            }
        },
    )
}

@Preview(showBackground = true)
@Composable
private fun EventCardPreview() {
    KindPrimeAppTheme {
        Column(modifier = Modifier.padding(16.dp)) {
            EventCard(
                event = EventEntity(
                    id = 1L,
                    title = "Reunión de equipo",
                    description = "Revisar los avances del proyecto y planificar la siguiente entrega.",
                    eventDate = defaultEventDateMillis(),
                    remainingDate = 0L,
                    completed = false,
                ),
                onToggleCompleted = {},
                onEdit = {},
                onDelete = {},
            )
        }
    }
}
