package pt.unl.fct.iadi.novaevents.controller

import org.springframework.http.HttpStatus
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.validation.BindException
import org.springframework.web.bind.MethodArgumentNotValidException
import pt.unl.fct.iadi.novaevents.model.EventType
import java.util.*

@ControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(NoSuchElementException::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun handleNotFound(ex: NoSuchElementException, model: Model): String {
        model.addAttribute("message", ex.message)
        return "error/404"
    }

    @ExceptionHandler(BindException::class)
    @ResponseStatus(HttpStatus.OK)
    fun handleBindException(ex: BindException, model: Model): String {

        model.addAttribute("eventForm", ex.target)
        model.addAttribute("types", EventType.values())

        return "events/form"
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    @ResponseStatus(HttpStatus.OK)
    fun handleValidationException(ex: MethodArgumentNotValidException, model: Model): String {

        model.addAttribute("eventForm", ex.bindingResult.target)
        model.addAttribute("types", EventType.values())

        return "events/form"
    }
}