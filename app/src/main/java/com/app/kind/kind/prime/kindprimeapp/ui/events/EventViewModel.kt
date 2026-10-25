package com.app.kind.kind.prime.kindprimeapp.ui.events

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.app.kind.kind.prime.kindprimeapp.data.EventDatabase
import com.app.kind.kind.prime.kindprimeapp.data.EventEntity
import com.app.kind.kind.prime.kindprimeapp.data.EventRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class EventViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = EventRepository(
        EventDatabase.getInstance(application).eventDao(),
    )

    val events: StateFlow<List<EventEntity>> = repository.events.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = emptyList(),
    )

    fun saveEvent(
        id: Long,
        title: String,
        description: String,
        eventDate: Long,
        completed: Boolean,
    ) {
        viewModelScope.launch {
            repository.save(
                EventEntity(
                    id = id,
                    title = title.trim(),
                    description = description.trim(),
                    eventDate = eventDate,
                    remainingDate = 0L,
                    completed = completed,
                ),
            )
        }
    }

    fun toggleCompleted(event: EventEntity) {
        viewModelScope.launch {
            repository.setCompleted(event, !event.completed)
        }
    }

    fun deleteEvent(event: EventEntity) {
        viewModelScope.launch {
            repository.delete(event)
        }
    }
}
