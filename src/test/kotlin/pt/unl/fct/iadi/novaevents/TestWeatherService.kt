package pt.unl.fct.iadi.novaevents

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.*
import pt.unl.fct.iadi.novaevents.client.WeatherClient
import pt.unl.fct.iadi.novaevents.service.WeatherService

class TestWeatherService {

    private val client: WeatherClient = mock()
    private val service = WeatherService(client)

    @Test
    fun `returns true when raining`() {
        whenever(client.isRaining("Lisbon")).thenReturn(true)

        val result = service.isRaining("Lisbon")

        assertTrue(result!!)
    }

    @Test
    fun `returns false when clear`() {
        whenever(client.isRaining("Lisbon")).thenReturn(false)

        val result = service.isRaining("Lisbon")

        assertFalse(result!!)
    }

    @Test
    fun `returns null when unavailable`() {
        whenever(client.isRaining("Lisbon")).thenReturn(null)

        val result = service.isRaining("Lisbon")

        assertNull(result)
    }
}