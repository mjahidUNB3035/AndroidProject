package ca.unb.mobiledev.weatherapp

import java.io.Serializable

data class WeatherX(
    val description: String,
    val icon: String,
    val id: Int,
    val main: String
): Serializable