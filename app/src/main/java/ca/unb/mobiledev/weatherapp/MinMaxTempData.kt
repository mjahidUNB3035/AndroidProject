package ca.unb.mobiledev.weatherapp

data class MinMaxTempData(
    val date: String,    // date
    val maxTemp: Int,  // Maximum temperature for the day
    val minTemp: Int,   // Minimum temperature for the day
    val humidity: Int  // humidity field
)
