package ca.unb.mobiledev.weatherapp

import java.io.Serializable

data class Wind(
    val deg: Int,
    val speed: Double
): Serializable