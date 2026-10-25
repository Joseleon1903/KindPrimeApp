package com.app.kind.kind.prime.kindprimeapp.data

import kotlinx.coroutines.flow.Flow

class EventRepository(private val eventDao: EventDao) {

    val events: Flow<List<EventEntity>> = eventDao.observeAll()

    /**
     * Crea o actualiza un evento. El campo [EventEntity.remainingDate] se calcula
     * automáticamente a partir de [EventEntity.eventDate].
     */
    suspend fun save(event: EventEntity) {
        val withRemaining = event.copy(
            remainingDate = event.eventDate - System.currentTimeMillis(),
        )
        if (event.id == 0L) {
            eventDao.insert(withRemaining)
        } else {
            eventDao.update(withRemaining)
        }
    }

    suspend fun setCompleted(event: EventEntity, completed: Boolean) {
        eventDao.update(
            event.copy(
                completed = completed,
                remainingDate = event.eventDate - System.currentTimeMillis(),
            ),
        )
    }

    suspend fun delete(event: EventEntity) {
        eventDao.delete(event)
    }
}
