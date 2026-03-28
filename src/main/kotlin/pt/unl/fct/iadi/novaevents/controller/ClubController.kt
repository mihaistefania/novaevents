package pt.unl.fct.iadi.novaevents.controller

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import pt.unl.fct.iadi.novaevents.service.ClubService
import pt.unl.fct.iadi.novaevents.service.EventService
import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException

@Controller
@RequestMapping("/clubs")
class ClubController(
    private val clubService: ClubService,
    private val eventService: EventService
) {

    @GetMapping
    fun listClubs(model: Model): String {

        val result = clubService.findAllWithEventCount()

        val clubs = result.map { it.first }
        val clubEventCounts = result.associate { (club, count) ->
            club.id to count
        }

        model.addAttribute("clubs", clubs)
        model.addAttribute("clubEventCounts", clubEventCounts)

        return "clubs/list"
    }

    @GetMapping("/{id}")
    fun clubDetail(@PathVariable id: Long, model: Model): String {

        val club = clubService.findByIdOrNull(id)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)

        val events = eventService.filter(null, id, null, null)

        model.addAttribute("club", club)
        model.addAttribute("events", events)

        return "clubs/detail"
    }
}