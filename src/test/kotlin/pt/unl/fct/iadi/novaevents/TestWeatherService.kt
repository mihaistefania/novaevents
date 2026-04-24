package pt.unl.fct.iadi.novaevents

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import pt.unl.fct.iadi.novaevents.client.WeatherClient
import pt.unl.fct.iadi.novaevents.service.WeatherService

class TestWeatherService {

    private val client: WeatherClient = mock(WeatherClient::class.java)
    private val service = WeatherService(client)

    @Test
    fun `returns true when raining`() {
        `when`(client.isRaining("Lisbon")).thenReturn(true)

        val result = service.isRaining("Lisbon")

        assertTrue(result!!)
    }

    @Test
    fun `returns false when clear`() {
        `when`(client.isRaining("Lisbon")).thenReturn(false)

        val result = service.isRaining("Lisbon")

        assertFalse(result!!)
    }

    @Test
    fun `returns null when unavailable`() {
        `when`(client.isRaining("Lisbon")).thenReturn(null)

        val result = service.isRaining("Lisbon")

        assertNull(result)
    }
}