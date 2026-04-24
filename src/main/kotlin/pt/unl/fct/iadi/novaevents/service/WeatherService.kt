package pt.unl.fct.iadi.novaevents.service

import org.springframework.stereotype.Service
import pt.unl.fct.iadi.novaevents.client.WeatherClient

@Service
class WeatherService(
    private val weatherClient: WeatherClient
) {

    fun isRaining(location: String): Boolean? {
        return try {
            weatherClient.isRaining(location)
        } catch (e: Exception) {
            null
        }
    }
}