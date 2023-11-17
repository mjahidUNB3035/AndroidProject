package ca.unb.mobiledev.weatherapp

//import android.telecom.Call
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
interface ApiInterface {
    @GET("weather")
    fun getWeatherData(
        @Query("q") city: String,
        @Query("appid") appid: String,
        @Query("units") units: String
    ): Call<WeatherApp>

    //5 Day Forecast
    @GET("forecast")
    fun getFiveDayWeatherData(
        @Query("q") city: String,
        @Query("appid") appid: String,
        @Query("units") units: String
    ): Call<NewWeatherApp> // Replace ForecastData with your actual data model class

    //fun getFiveDayWeatherData(city: String, s: String, s1: String): Any
    //fun getFiveDayWeatherData(city: String, s: String, s1: String): Any
}