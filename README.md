# KindPrimeApp

Aplicación Android desarrollada con Kotlin y Jetpack Compose.

## Funcionalidades

- Navegación inferior adaptativa (Inicio, Favoritos, Perfil).
- Listado de eventos en Inicio, ordenado por fecha y con los completados al final.
- Crear eventos con título, descripción y fecha (selector de fecha de Material 3).
- Editar y eliminar eventos (con diálogo de confirmación).
- Marcar eventos como completados.
- Indicador de tiempo restante: días pendientes, «Vence hoy» o días vencidos.
- Persistencia local con Room (SQLite), sin conexión a internet.

## Paleta de colores

| Elemento | Color |
|---|---|
| Fondo general | `#121412` |
| Tarjetas / superficies | `#202520` |
| Botón principal | `#B8F34A` |
| Texto principal | `#F4F6F0` |
| Texto secundario | `#929B90` |
| Progreso completado | `#B8F34A` |
| Advertencias | `#F5B544` |
| Errores | `#FF6B6B` |

El tema está definido en `ui/theme/Color.kt` y `ui/theme/Theme.kt`. Es un tema
oscuro por diseño y se aplica siempre, sin color dinámico ni variante clara.

## Estructura

```
app/src/main/java/com/app/kind/kind/prime/kindprimeapp/
├── MainActivity.kt                  # Navegación principal
├── data/
│   ├── EventEntity.kt               # Entidad: id, title, description, eventDate, remainingDate, completed
│   ├── EventDao.kt                  # Consultas y CRUD
│   ├── EventDatabase.kt             # Base de datos Room (kindprime.db)
│   └── EventRepository.kt           # Lógica de acceso a datos
└── ui/
    ├── PlaceholderScreen.kt
    └── events/
        ├── EventViewModel.kt        # Estado y operaciones de eventos
        ├── HomeScreen.kt            # Listado, FAB, edición y eliminación
        ├── EventFormDialog.kt       # Formulario y selector de fecha
        └── EventDateUtils.kt        # Formato y cálculo de días restantes
```

## Requisitos

- Android Studio con AGP 9.4.1, Gradle 9.6.0 y Kotlin 2.2.10.
- minSdk 30 / targetSdk 37.

## Base de datos

Room versión 2.7.2 con KSP. La base de datos `kindprime.db` se crea en el
almacenamiento interno de la app. `EventEntity.remainingDate` guarda los
milisegundos restantes en el momento de guardar el evento; la interfaz calcula
el tiempo restante en vivo a partir de `eventDate`.
