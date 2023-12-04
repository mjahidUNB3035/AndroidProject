package ca.unb.mobiledev.weatherapp

data class MinMaxTempData(
    val date: String,    // Assuming you want to display the date
    val maxTemp: Float,  // Maximum temperature for the day
    val minTemp: Float,   // Minimum temperature for the day
    val humidity: Int  // Add humidity field
)
