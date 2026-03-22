package pt.unl.fct.iadi.novaevents.controller

import jakarta.validation.Valid
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*
import pt.unl.fct.iadi.novaevents.controller.dto.EventForm
import pt.unl.fct.iadi.novaevents.model.Event
import pt.unl.fct.iadi.novaevents.model.EventType
import pt.unl.fct.iadi.novaevents.service.ClubService
import pt.unl.fct.iadi.novaevents.service.EventService
import java.time.LocalDate

@Controller
@RequestMapping("/events")
class EventController(
    private val eventService: EventService,
    private val clubService: ClubService
) {

    @GetMapping
    fun listEvents(
        @RequestParam(required = false) type: EventType?,
        @RequestParam(required = false) clubId: Long?,
        @RequestParam(required = false) from: LocalDate?,
        @RequestParam(required = false) to: LocalDate?,
        model: Model
    ): String {

        val events = eventService.filter(type, clubId, from, to)

        model.addAttribute("events", events)
        model.addAttribute("clubs", clubService.getAll())

        return "events/list"
    }

    @GetMapping("/{id}")
    fun eventDetail(@PathVariable id: Long, model: Model): String {
        val event = eventService.getById(id)
        val club = clubService.getById(event.clubId)

        model.addAttribute("event", event)
        model.addAttribute("club", club)

        return "events/detail"
    }

    @GetMapping("/create/{clubId}")
    fun showCreateForm(@PathVariable clubId: Long, model: Model): String {
        model.addAttribute("eventForm", EventForm())
        model.addAttribute("clubId", clubId)
        model.addAttribute("types", EventType.values())
        return "events/form"
    }

    @PostMapping("/create/{clubId}")
    fun createEvent(
        @PathVariable clubId: Long,
        @Valid @ModelAttribute eventForm: EventForm,
        bindingResult: BindingResult,
        model: Model
    ): String {

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


        val created = eventService.create(event)

        return "redirect:/events/${created.id}"
    }

    @GetMapping("/edit/{id}")
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

    @PutMapping("/edit/{id}")
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

    @GetMapping("/delete/{id}")
    fun confirmDelete(@PathVariable id: Long, model: Model): String {
        val event = eventService.getById(id)
        model.addAttribute("event", event)
        return "events/delete"
    }

    @DeleteMapping("/delete/{id}")
    fun deleteEvent(@PathVariable id: Long): String {
        val clubId = eventService.getById(id).clubId
        eventService.delete(id)
        return "redirect:/clubs/$clubId"
    }
}