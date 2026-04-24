package pt.unl.fct.iadi.novaevents.controller

import org.springframework.http.MediaType
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody
import pt.unl.fct.iadi.novaevents.service.WeatherService

@Controller
class WeatherController(
    private val weatherService: WeatherService
) {

    @GetMapping("/api/weather", produces = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseBody
    fun getWeatherJson(@RequestParam location: String): Map<String, Boolean?> {

        val raining = weatherService.isRaining(location)

        return mapOf("raining" to raining)
    }

    @GetMapping("/api/weather", produces = [MediaType.TEXT_HTML_VALUE])
    fun getWeatherHtml(
        @RequestParam location: String,
        model: Model
    ): String {

        val raining = weatherService.isRaining(location)

        val weatherState = when (raining) {
            true -> "raining"
            false -> "clear"
            null -> "unknown"
        }

        model.addAttribute("weather", weatherState)

        return "fragments/weather :: result"
    }
}