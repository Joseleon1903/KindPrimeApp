package com.app.kind.kind.prime.kindprimeapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Evento almacenado en la base de datos local.
 *
 * @property eventDate fecha del evento en milisegundos (inicio del día).
 * @property remainingDate milisegundos restantes hasta el evento en el momento de guardarlo.
 *         Puede ser negativo si la fecha ya pasó.
 */
@Entity(tableName = "events")
data class EventEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val title: String,
    val description: String,
    val eventDate: Long,
    val remainingDate: Long,
    val completed: Boolean = false,
)
