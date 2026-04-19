package pt.unl.fct.iadi.novaevents
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import pt.unl.fct.iadi.novaevents.repository.EventRepository

@Component
class EventSecurity(
    private val eventRepository: EventRepository
) {

    fun isOwner(eventId: Long, username: String): Boolean {
        val event = eventRepository.findById(eventId).orElse(null) ?: return false
        return event.owner?.username == username
    }

    fun isOwnerOrAdmin(eventId: Long, username: String): Boolean {

        val event = eventRepository.findById(eventId).orElse(null) ?: return false

        val auth = SecurityContextHolder.getContext().authentication

        val isAdmin = auth.authorities.any {
            it.authority == "ROLE_ADMIN"
        }

        return event.owner?.username == username || isAdmin
    }
}