package pt.unl.fct.iadi.novaevents.client

data class WeatherResponse(
    val weather: List<WeatherItem>
)

data class WeatherItem(
    val main: String,
    val description: String
)