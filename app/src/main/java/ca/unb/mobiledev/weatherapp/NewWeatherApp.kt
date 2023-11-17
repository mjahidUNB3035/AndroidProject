package ca.unb.mobiledev.weatherapp

data class NewWeatherApp(
    val city: City,
    val cnt: Int,
    val cod: String,
    val list: List<ForecastData>,
    val message: Int
)