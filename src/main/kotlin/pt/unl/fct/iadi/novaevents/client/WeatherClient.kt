package pt.unl.fct.iadi.novaevents.client

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClient

@Component
class WeatherClient(
    private val restClientBuilder: RestClient.Builder,
    @Value("\${weather.api.key}") private val apiKey: String
) {

    fun isRaining(location: String): Boolean? {
        return try {

            val client = restClientBuilder.build()

            val response = client.get()
                .uri(
                    "https://api.openweathermap.org/data/2.5/weather?q={loc}&appid={key}",
                    location, apiKey
                )
                .retrieve()
                .body(Map::class.java) as? Map<*, *>
                ?: return null

            val weatherList = response["weather"] as? List<*> ?: return null
            val first = weatherList.firstOrNull() as? Map<*, *> ?: return null

            val main = first["main"] as? String ?: return null

            main.equals("Rain", ignoreCase = true)

        } catch (e: Exception) {
            null
        }
    }
}