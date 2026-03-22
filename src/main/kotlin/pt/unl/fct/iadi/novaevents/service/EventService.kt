package pt.unl.fct.iadi.novaevents.service

import org.springframework.stereotype.Service
import pt.unl.fct.iadi.novaevents.model.*
import java.time.LocalDate

@Service
class EventService {

    private val events = mutableListOf<Event>()
    private var nextId = 1L

    init {
        events.add(
            Event(
                id = 1,
                clubId = 1,
                name = "Beginner's Chess Workshop",
                date = LocalDate.now(),
                location = "Room A",
                type = EventType.WORKSHOP,
                description = "Learn basics of chess"
            )
        )

        events.add(
            Event(
                id = 2,
                clubId = 1,
                name = "Spring Chess Tournament",
                date = LocalDate.now(),
                location = "Main Hall",
                type = EventType.COMPETITION,
                description = "Annual tournament"
            )
        )

        events.add(
            Event(3, 2, "Robotics Workshop", LocalDate.now(), "Engineering Lab", EventType.WORKSHOP, "Build robots")
        )

        events.add(
            Event(4, 3, "Photography Walk", LocalDate.now(), "City Center", EventType.SOCIAL, "Outdoor shooting")
        )

        events.add(
            Event(5, 4, "Mountain Hike", LocalDate.now(), "Mountain Trail", EventType.SOCIAL, "Hiking adventure")
        )

        events.add(
            Event(6, 5, "Film Night", LocalDate.now(), "Cinema Room", EventType.SOCIAL, "Watch movies")
        )

        nextId = 7
    }

    fun getAll(): List<Event> = events

    fun getById(id: Long): Event {
        return events.find { it.id == id }
            ?: throw NoSuchElementException("Event not found")
    }

    fun getByClub(clubId: Long): List<Event> {
        return events.filter { it.clubId == clubId }
    }

    fun create(event: Event): Event {

        if (events.any { it.name.equals(event.name, ignoreCase = true) }) {
            throw IllegalArgumentException("An event with this name already exists")
        }

        val newEvent = event.copy(id = nextId++)
        events.add(newEvent)

        return newEvent
    }

    fun update(id: Long, updated: Event) {

        val existing = getById(id)

        // Duplicate check (exclude current)
        if (events.any {
                it.id != id && it.name.equals(updated.name, ignoreCase = true)
            }) {
            throw IllegalArgumentException("An event with this name already exists")
        }

        existing.name = updated.name
        existing.date = updated.date
        existing.location = updated.location
        existing.type = updated.type
        existing.description = updated.description
    }

    fun delete(id: Long) {
        val removed = events.removeIf { it.id == id }

        if (!removed) {
            throw NoSuchElementException("Event not found")
        }
    }

    fun filter(
        type: EventType?,
        clubId: Long?,
        from: LocalDate?,
        to: LocalDate?
    ): List<Event> {

        return events.filter {

            (type == null || it.type == type) &&
                    (clubId == null || it.clubId == clubId) &&
                    (from == null || !it.date.isBefore(from)) &&
                    (to == null || !it.date.isAfter(to))
        }
    }
}