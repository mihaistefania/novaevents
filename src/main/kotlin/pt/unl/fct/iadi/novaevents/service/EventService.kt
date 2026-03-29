package pt.unl.fct.iadi.novaevents.service

import org.springframework.stereotype.Service
import pt.unl.fct.iadi.novaevents.model.Event
import pt.unl.fct.iadi.novaevents.model.EventType
import pt.unl.fct.iadi.novaevents.repository.EventRepository
import java.time.LocalDate

@Service
class EventService(
    private val eventRepository: EventRepository
) {

    fun findAll(): List<Event> = eventRepository.findAll()

    fun getById(id: Long): Event =
        eventRepository.findById(id)
            .orElseThrow { NoSuchElementException("Event not found") }

    fun create(event: Event): Event {
        return eventRepository.save(event)
    }

    fun update(id: Long, updated: Event): Event {
        val existing = getById(id)

        existing.name = updated.name
        existing.date = updated.date
        existing.location = updated.location
        existing.description = updated.description
        existing.type = updated.type
        existing.club = updated.club

        return eventRepository.save(existing)
    }

    fun delete(id: Long) {
        if (!eventRepository.existsById(id)) {
            throw NoSuchElementException("Event not found")
        }
        eventRepository.deleteById(id)
    }

    fun filter(
        type: EventType?,
        clubId: Long?,
        from: LocalDate?,
        to: LocalDate?
    ): List<Event> {

        val events = when {
            type != null && clubId != null ->
                eventRepository.findByClub_IdAndType_Id(clubId, type.id)

            type != null ->
                eventRepository.findByType_Id(type.id)

            clubId != null ->
                eventRepository.findByClub_Id(clubId)

            else ->
                eventRepository.findAll()
        }

        return events.filter {
            (from == null || !it.date.isBefore(from)) &&
                    (to == null || !it.date.isAfter(to))
        }
    }
}