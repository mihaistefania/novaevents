package pt.unl.fct.iadi.novaevents.controller

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import pt.unl.fct.iadi.novaevents.service.ClubService
import pt.unl.fct.iadi.novaevents.service.EventService

@Controller
@RequestMapping("/clubs")
class ClubController(
    private val clubService: ClubService,
    private val eventService: EventService
) {

    @GetMapping
    fun listClubs(model: Model): String {
        model.addAttribute("clubs", clubService.findAll())
        return "clubs/list"
    }

    @GetMapping("/{id}")
    fun clubDetail(@PathVariable id: Long, model: Model): String {
        val club = clubService.findById(id)

        val events = eventService.filter(
            type = null,
            clubId = id,
            from = null,
            to = null
        )

        model.addAttribute("club", club)
        model.addAttribute("events", events)

        return "clubs/detail"
    }
}