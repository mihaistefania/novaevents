package pt.unl.fct.iadi.novaevents.controller.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import org.springframework.format.annotation.DateTimeFormat
import java.time.LocalDate

data class EventForm(

    @field:NotBlank(message = "Name is required")
    var name: String? = null,

    @field:NotNull(message = "Date is required")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    var date: LocalDate? = null,

    var location: String? = null,

    @field:NotNull(message = "Event type is required")
    var typeId: Long? = null,

    var description: String? = null,

    var clubId: Long? = null
)