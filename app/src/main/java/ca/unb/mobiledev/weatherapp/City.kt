package ca.unb.mobiledev.weatherapp

data class City(
    val coord: CoordX,
    val country: String,
    val id: Int,
    val name: String,
    val population: Int,
    val sunrise: Int,
    val sunset: Int,
    val timezone: Int
)