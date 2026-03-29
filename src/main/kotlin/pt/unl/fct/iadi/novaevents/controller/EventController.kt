package pt.unl.fct.iadi.novaevents.controller

import jakarta.validation.Valid
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.ui.ModelMap
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*
import pt.unl.fct.iadi.novaevents.controller.dto.EventForm
import pt.unl.fct.iadi.novaevents.model.Event
import pt.unl.fct.iadi.novaevents.repository.EventTypeRepository
import pt.unl.fct.iadi.novaevents.service.ClubService
import pt.unl.fct.iadi.novaevents.service.EventService
import java.time.LocalDate
import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException
import org.springframework.web.servlet.mvc.support.RedirectAttributes

@Controller
class EventController(
    private val eventService: EventService,
    private val clubService: ClubService,
    private val eventTypeRepository: EventTypeRepository
) {

    @GetMapping("/events")
    fun listEvents(
        @RequestParam(required = false) type: String?,
        @RequestParam(required = false) clubId: String?,
        @RequestParam(required = false) from: String?,
        @RequestParam(required = false) to: String?,
        model: Model
    ): String {

        val parsedType = type?.let {
            eventTypeRepository.findAll().find { t ->
                t.name.equals(it, ignoreCase = true)
            }
        }

        val parsedClubId = clubId?.toLongOrNull()

        val parsedFrom = try { from?.let { LocalDate.parse(it) } } catch (_: Exception) { null }
        val parsedTo = try { to?.let { LocalDate.parse(it) } } catch (_: Exception) { null }

        val events = eventService.filter(parsedType, parsedClubId, parsedFrom, parsedTo)

        model.addAttribute("events", events)
        model.addAttribute("clubs", clubService.findAll())
        model.addAttribute("types", eventTypeRepository.findAll())

        return "events/list"
    }

    @GetMapping("/events/{id}")
    fun detail(@PathVariable id: Long, model: ModelMap): String {

        val event = try {
            eventService.getById(id)
        } catch (e: NoSuchElementException) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND)
        }

        model["event"] = event
        model["club"] = event.club

        return "events/detail"
    }

    @GetMapping("/events/create/{clubId}")
    fun showCreateForm(
        @PathVariable clubId: Long,
        model: Model
    ): String {

        val form = EventForm()
        form.clubId = clubId

        model.addAttribute("eventForm", form)
        model.addAttribute("clubId", clubId)
        model.addAttribute("types", eventTypeRepository.findAll())

        return "events/create-form"
    }

    @PostMapping("/clubs/{clubId}/events")
    fun createEvent(
        @PathVariable clubId: Long,
        @Valid @ModelAttribute eventForm: EventForm,
        bindingResult: BindingResult,
        model: Model
    ): String {

        if (eventForm.typeId == null) {
            bindingResult.rejectValue("typeId", "error.typeId", "Event type is required")
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("types", eventTypeRepository.findAll())
            model.addAttribute("clubId", clubId)
            return "events/create-form"
        }

        val club = clubService.findById(clubId)
        val type = eventTypeRepository.findById(eventForm.typeId!!).orElseThrow()

        val event = Event(
            id = 0,
            name = eventForm.name!!,
            date = eventForm.date!!,
            location = eventForm.location,
            description = eventForm.description,
            club = club,
            type = type
        )

        return try {
            val created = eventService.create(event)
            "redirect:/clubs/$clubId/events/${created.id}"
        } catch (e: IllegalArgumentException) {

            bindingResult.rejectValue("name", "error.name", e.message!!)

            model.addAttribute("types", eventTypeRepository.findAll())
            model.addAttribute("clubId", clubId)

            "events/create-form"
        }
    }

    @GetMapping("/clubs/{clubId}/events/{id}/edit")
    fun showEditForm(
        @PathVariable clubId: Long,
        @PathVariable id: Long,
        model: Model
    ): String {

        val event = eventService.getById(id)

        val form = EventForm(
            name = event.name,
            date = event.date,
            location = event.location,
            description = event.description,
            typeId = event.type.id
        )

        model.addAttribute("eventForm", form)
        model.addAttribute("eventId", id)
        model.addAttribute("clubId", clubId)
        model.addAttribute("types", eventTypeRepository.findAll())

        return "events/edit-form"
    }

    @PostMapping("/clubs/{clubId}/events/{id}")
    fun updateEvent(
        @PathVariable clubId: Long,
        @PathVariable id: Long,
        @Valid @ModelAttribute eventForm: EventForm,
        bindingResult: BindingResult,
        model: Model
    ): String {

        if (eventForm.typeId == null) {
            bindingResult.rejectValue("typeId", "error.typeId", "Event type is required")
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("types", eventTypeRepository.findAll())
            model.addAttribute("eventId", id)
            model.addAttribute("clubId", clubId)
            return "events/edit-form"
        }

        val club = clubService.findById(clubId)
        val type = eventTypeRepository.findById(eventForm.typeId!!).orElseThrow()

        val updated = Event(
            id = id,
            name = eventForm.name!!,
            date = eventForm.date!!,
            location = eventForm.location,
            description = eventForm.description,
            club = club,
            type = type
        )

        return try {
            eventService.update(id, updated)
            "redirect:/clubs/$clubId/events/$id"
        } catch (e: IllegalArgumentException) {

            bindingResult.rejectValue("name", "error.name", e.message!!)

            model.addAttribute("types", eventTypeRepository.findAll())
            model.addAttribute("eventId", id)
            model.addAttribute("clubId", clubId)

            "events/edit-form"
        }
    }

    @PostMapping("/clubs/{clubId}/events/{id}/delete")
    fun deleteEvent(
        @PathVariable clubId: Long,
        @PathVariable id: Long
    ): String {

        eventService.delete(id)

        return "redirect:/clubs/$clubId"
    }
    @GetMapping("/clubs/{clubId}/events/{id}")
    fun detailFromClub(
        @PathVariable clubId: Long,
        @PathVariable id: Long,
        model: Model
    ): String {

        val event = try {
            eventService.getById(id)
        } catch (e: NoSuchElementException) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND)
        }

        model.addAttribute("event", event)
        model.addAttribute("club", event.club)

        return "events/detail"
    }
}