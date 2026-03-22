package pt.unl.fct.iadi.novaevents.controller

import jakarta.validation.Valid
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.ui.ModelMap
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*
import pt.unl.fct.iadi.novaevents.controller.dto.EventForm
import pt.unl.fct.iadi.novaevents.model.Event
import pt.unl.fct.iadi.novaevents.model.EventType
import pt.unl.fct.iadi.novaevents.service.ClubService
import pt.unl.fct.iadi.novaevents.service.EventService
import java.time.LocalDate
import org.springframework.format.annotation.DateTimeFormat

@Controller
@RequestMapping
class EventController(
    private val eventService: EventService,
    private val clubService: ClubService
) {

    @GetMapping("/events")
    fun listEvents(
        @RequestParam(required = false) type: String?,
        @RequestParam(required = false) clubId: String?,
        @RequestParam(required = false) from: String?,
        @RequestParam(required = false) to: String?,
        model: Model
    ): String {

        val parsedType = type
            ?.uppercase()
            ?.let { EventType.values().find { e -> e.name == it } }

        val parsedClubId = clubId?.toLongOrNull()

        val parsedFrom = try { from?.let { LocalDate.parse(it) } } catch (e: Exception) { null }
        val parsedTo = try { to?.let { LocalDate.parse(it) } } catch (e: Exception) { null }

        val events = eventService.filter(parsedType, parsedClubId, parsedFrom, parsedTo)

        model.addAttribute("events", events)
        model.addAttribute("clubs", clubService.getAll())

        return "events/list"
    }

    @GetMapping("/events/{id}")
    fun detail(@PathVariable id: Long, model: ModelMap): String {

        val event = eventService.getById(id)
        val club = clubService.getById(event.clubId)

        model["event"] = event
        model["club"] = club

        return "events/detail"
    }

    @GetMapping("/events/create/{clubId}")
    fun showCreateForm(@PathVariable clubId: Long, model: Model): String {

        val form = EventForm()
        form.clubId = clubId

        model.addAttribute("eventForm", form)
        model.addAttribute("clubId", clubId)
        model.addAttribute("types", EventType.values())

        return "events/form"
    }



    @GetMapping("/events/edit/{id}")
    fun showEditForm(@PathVariable id: Long, model: Model): String {

        val event = eventService.getById(id)

        val form = EventForm(
            name = event.name,
            date = event.date,
            location = event.location,
            type = event.type,
            description = event.description
        )

        model.addAttribute("eventForm", form)
        model.addAttribute("eventId", id)
        model.addAttribute("types", EventType.values())

        return "events/edit-form"
    }

    @PutMapping("/events/edit/{id}")
    fun updateEvent(
        @PathVariable id: Long,
        @Valid @ModelAttribute eventForm: EventForm,
        bindingResult: BindingResult,
        model: Model
    ): String {

        if (bindingResult.hasErrors()) {
            model.addAttribute("types", EventType.values())
            model.addAttribute("eventId", id)
            return "events/edit-form"
        }

        val updated = Event(
            id = id,
            clubId = eventService.getById(id).clubId,
            name = eventForm.name!!,
            date = eventForm.date!!,
            location = eventForm.location,
            type = eventForm.type!!,
            description = eventForm.description
        )

        eventService.update(id, updated)

        return "redirect:/events/$id"
    }

    @GetMapping("/events/delete/{id}")
    fun confirmDelete(@PathVariable id: Long, model: Model): String {
        val event = eventService.getById(id)
        model.addAttribute("event", event)
        return "events/delete"
    }

    @DeleteMapping("/events/delete/{id}")
    fun deleteEvent(@PathVariable id: Long): String {
        val clubId = eventService.getById(id).clubId
        eventService.delete(id)
        return "redirect:/clubs/$clubId"
    }

    @GetMapping("/clubs/{clubId}/events/{eventId}")
    fun detailWithClub(
        @PathVariable clubId: Long,
        @PathVariable eventId: Long,
        model: ModelMap
    ): String {

        val event = eventService.getById(eventId)
        val club = clubService.getById(clubId)

        model["event"] = event
        model["club"] = club

        return "events/detail"
    }

    @PostMapping("/events")
    fun createEvent(
        @Valid @ModelAttribute eventForm: EventForm,
        bindingResult: BindingResult,
        model: Model
    ): String {

        val clubId = eventForm.clubId
            ?: throw IllegalArgumentException("ClubId missing")

        if (bindingResult.hasErrors()) {
            model.addAttribute("types", EventType.values())
            model.addAttribute("clubId", clubId)
            return "events/form"
        }

        val event = Event(
            id = 0,
            clubId = clubId,
            name = eventForm.name!!,
            date = eventForm.date!!,
            location = eventForm.location,
            type = eventForm.type!!,
            description = eventForm.description
        )

        return try {
            var created = eventService.create(event)
            return "redirect:/events/${created.id}"
        } catch (e: IllegalArgumentException) {
            bindingResult.rejectValue("name", "error.name", e.message!!)
            model.addAttribute("types", EventType.values())
            model.addAttribute("clubId", clubId)
            "events/form"
        }
    }
}