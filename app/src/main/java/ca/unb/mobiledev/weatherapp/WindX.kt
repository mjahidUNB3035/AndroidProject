package ca.unb.mobiledev.weatherapp

import java.io.Serializable

data class WindX(
    val deg: Int,
    val gust: Double,
    val speed: Double
): Serializable