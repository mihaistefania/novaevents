package pt.unl.fct.iadi.novaevents

import org.junit.jupiter.api.Test
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import pt.unl.fct.iadi.novaevents.controller.WeatherController
import pt.unl.fct.iadi.novaevents.service.WeatherService

@WebMvcTest(WeatherController::class)
class TestWeatherController {

    @Autowired
    lateinit var mockMvc: MockMvc

    @MockBean
    lateinit var weatherService: WeatherService

    @Test
    fun `returns JSON response`() {

        whenever(weatherService.isRaining("Lisbon")).thenReturn(true)

        mockMvc.get("/api/weather?location=Lisbon") {
            accept = org.springframework.http.MediaType.APPLICATION_JSON
        }
            .andExpect {
                status { isOk() }
                jsonPath("$.raining") { value(true) }
            }
    }
}