package ca.unb.mobiledev.weatherapp

import java.io.Serializable

data class ForecastData(
    val clouds: CloudsX,
    val dt: Int,
    val dt_txt: String,
    val main: MainX,
    val pop: Double,
    val rain: Rain,
    val snow: Snow,
    val sys: SysX,
    val visibility: Int,
    val weather: List<WeatherX>,
    val wind: WindX
) : Serializable {
    data class Main(val temp: Double, val humidity: Int) : Serializable
    data class Weather(val description: String, val icon: String) : Serializable
    data class Wind(val speed: Double, val deg: Int) : Serializable

    data class City(
        val coord: CoordX,
        val country: String,
        val id: Int,
        val name: String,
        val population: Int,
        val sunrise: Int,
        val sunset: Int,
        val timezone: Int
    ) : Serializable
    data class Clouds(
        val all: Int
    ): Serializable
    data class CloudsX(
        val all: Int
    ): Serializable
    data class Coord(
        val lat: Double,
        val lon: Double
    ): Serializable
    data class CoordX(
        val lat: Double,
        val lon: Double
    ): Serializable
    data class MainX(
        val feels_like: Double,
        val grnd_level: Int,
        val humidity: Int,
        val pressure: Int,
        val sea_level: Int,
        val temp: Double,
        val temp_kf: Double,
        val temp_max: Double,
        val temp_min: Double
    ): Serializable
    data class NewWeatherApp(
        val city: ca.unb.mobiledev.weatherapp.City,
        val cnt: Int,
        val cod: String,
        val list: List<ForecastData>,
        val message: Int
    ): Serializable
    data class Rain(
        val `3h`: Double
    ): Serializable

    data class Snow(
        val `3h`: Double
    ): Serializable
    data class Sys(
        val country: String,
        val id: Int,
        val sunrise: Int,
        val sunset: Int,
        val type: Int
    ): Serializable
    data class SysX(
        val pod: String
    ): Serializable

    data class WeatherApp(
        val base: String,
        val clouds: ca.unb.mobiledev.weatherapp.Clouds,
        val cod: Int,
        val coord: ca.unb.mobiledev.weatherapp.Coord,
        val dt: Int,
        val id: Int,
        val main: ca.unb.mobiledev.weatherapp.Main,
        val name: String,
        val sys: ca.unb.mobiledev.weatherapp.Sys,
        val timezone: Int,
        val visibility: Int,
        val weather: List<ca.unb.mobiledev.weatherapp.Weather>,
        val wind: ca.unb.mobiledev.weatherapp.Wind
    ): Serializable

    data class WeatherX(
        val description: String,
        val icon: String,
        val id: Int,
        val main: String
    ): Serializable
    data class WindX(
        val deg: Int,
        val gust: Double,
        val speed: Double
    ): Serializable

}


//package ca.unb.mobiledev.weatherapp
//
//data class (
//    val clouds: CloudsX,
//    val dt: Int,
//    val dt_txt: String,
//    val main: MainX,
//    val pop: Double,
//    val rain: Rain,
//    val snow: Snow,
//    val sys: SysX,
//    val visibility: Int,
//    val weather: List<WeatherX>,
//    val wind: WindX
//)