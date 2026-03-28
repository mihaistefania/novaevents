package pt.unl.fct.iadi.novaevents.controller

import org.springframework.http.HttpStatus
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.validation.BindException
import org.springframework.web.bind.MethodArgumentNotValidException
import pt.unl.fct.iadi.novaevents.repository.EventTypeRepository

@Controller
class GlobalExceptionHandler(
    private val eventTypeRepository: EventTypeRepository
) {

    @ExceptionHandler(BindException::class)
    @ResponseStatus(HttpStatus.OK)
    fun handleBindException(ex: BindException, model: Model): String {

        model.addAttribute("eventForm", ex.target)
        model.addAttribute("types", eventTypeRepository.findAll())

        return "events/form"
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    @ResponseStatus(HttpStatus.OK)
    fun handleValidationException(ex: MethodArgumentNotValidException, model: Model): String {

        model.addAttribute("eventForm", ex.bindingResult.target)
        model.addAttribute("types", eventTypeRepository.findAll())

        return "events/form"
    }
}