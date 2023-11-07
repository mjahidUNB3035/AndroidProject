package ca.unb.mobiledev.weatherapp

import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
//import androidx.appcompat.widget.SearchView
import android.widget.SearchView
import androidx.core.app.ActivityCompat
import ca.unb.mobiledev.weatherapp.databinding.ActivityMainBinding
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.Task
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import android.Manifest
import android.location.LocationRequest
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult


class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    companion object {
        private const val REQUEST_LOCATION_PERMISSION = 1 // or any other integer
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setContentView(binding.root)
        // Ensure you have the right permissions before fetching the weather data
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // Permissions are granted, you can fetch the last known location
            getLastKnownLocation()
        } else {
            // Permissions are not granted, request them
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_LOCATION_PERMISSION)
        }
        searchCity()
    }
    //This will serve realtime location
//    val locationRequest = LocationRequest.create().apply {
//        interval = 10000 // Update interval
//        fastestInterval = 5000 // Fastest update interval
//        priority = LocationRequest.PRIORITY_HIGH_ACCURACY
//        maxWaitTime = 60000 // Max wait time for batched updates (optional)
//    }
//
//    private val locationCallback = object : LocationCallback() {
//        override fun onLocationResult(locationResult: LocationResult?) {
//            locationResult ?: return
//            for (location in locationResult.locations) {
//                // If you only need one location, no need to continue updates, stop them
//                if (location != null) {
//                    // Now you have the location
//                    CoroutineScope(Dispatchers.Main).launch {
//                        val cityName = getCityName(this@MainActivity, location.latitude, location.longitude)
//                        if (cityName != null) {
//                            fetchWeatherData(cityName)
//                        }
//                    }
//                    val fusedLocationProviderClient=""
//                    fusedLocationProviderClient.removeLocationUpdates(this)
//                    break
//                }
//            }
//        }
//    }


//    private fun startLocationUpdates() {
//        if (ActivityCompat.checkSelfPermission(
//                this,
//                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
//            ActivityCompat.checkSelfPermission(
//                this,
//                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//
//            ActivityCompat.requestPermissions(this,
//                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
//                REQUEST_LOCATION_PERMISSION)
//            return
//        }
//        fusedLocationProviderClient.requestLocationUpdates(
//            locationRequest,
//            locationCallback,
//            Looper.getMainLooper()
//        )
//    }

    // Request location updates
    private fun fetchLastKnownLocation() {
        suspend fun getCityName(context: Context, latitude: Double, longitude: Double): String? {
            return withContext(Dispatchers.IO) {
                try {
                    val geocoder = Geocoder(context, Locale.getDefault())
                    val addresses = geocoder.getFromLocation(latitude, longitude, 1)
                    if (addresses?.isNotEmpty() == true) {
                        addresses[0].locality
                    } else {
                        null
                    }
                } catch (e: IOException) {
                    // Handle the IOException
                    null
                }
            }
        }

    }
    //Current Location

    private fun getLastKnownLocation() {
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            val locationResult: Task<Location> = fusedLocationClient.lastLocation
            locationResult.addOnSuccessListener { location ->
                location?.let {
                    CoroutineScope(Dispatchers.Main).launch {
                        val cityName = getCityName(it.latitude, it.longitude)
                        cityName?.let { name ->
                            fetchWeatherData(name)
                        }
                    }
                }
            }.addOnFailureListener {
                // Handle the case where you don't get the location
            }
        } else {
            // Request permissions from the user
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_LOCATION_PERMISSION)
        }
    }

    private suspend fun getCityName(latitude: Double, longitude: Double): String? {
        return withContext(Dispatchers.IO) {
            try {
                val geocoder = Geocoder(this@MainActivity, Locale.getDefault())
                val addresses = geocoder.getFromLocation(latitude, longitude, 1)
                if (addresses?.isNotEmpty() == true) {
                    addresses[0].locality
                } else {
                    null
                }
            } catch (e: IOException) {
                // Handle the IOException
                Log.e("MainActivity", "Unable to get city name", e)
                null
            }
        }
    }


    private fun searchCity() {
        val searchView = binding.searchView
            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
                override fun onQueryTextSubmit(query: String?): Boolean {
                    if (query != null) {
                        fetchWeatherData(query)
                    }
                    return true
                }

                override  fun onQueryTextChange(newText: String?): Boolean {
                    return true
                }

            })
    }

    private fun fetchWeatherData(city:String) {
        //set retrofit
        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://api.openweathermap.org/data/2.5/")
            .build().create(ApiInterface::class.java)
        val response = retrofit.getWeatherData(city, "a172f58aec61e7a63e67507b5c61c057", "metric" )
        response.enqueue(object : Callback<WeatherApp> {
            override fun onResponse(call: Call<WeatherApp>, response: Response<WeatherApp>) {
                val responseBody = response.body()
                if(response.isSuccessful && responseBody != null)
                {
                    val temperature = responseBody.main.temp.toString()
                    val humidity = responseBody.main.humidity
                    val windSpeed = responseBody.wind.speed
                    val sunRise = responseBody.sys.sunrise
                    val sunSet = responseBody.sys.sunset
                    val seaLevel = responseBody.main.pressure
                    val maxTemp = responseBody.main.temp_max
                    val minTemp = responseBody.main.temp_min
                    val condition = responseBody.weather.firstOrNull()?.main?:"unknown"
                   // Log.d("TAG", "onResponse: $temperature") //temp is temperature
                    binding.temp.text = "$temperature"
                    binding.weather.text = condition
                    binding.maxTemp.text = "Max Temp: $maxTemp"
                    binding.minTemp.text = "Min Temp: $minTemp"
                    binding.humidity.text = "Humidity: $humidity %"
                    binding.wind.text = "Max Temp: $windSpeed"
                    binding.sunrise.text = "$sunRise"
                    binding.sunset.text = "$sunSet"
                    binding.sea.text = "$seaLevel hpma"
                    binding.condition.text = condition
                    binding.day.text = dayName(System.currentTimeMillis())
                    binding.date.text = date(System.currentTimeMillis())
                     binding.cityName.text = "$city"

                    changeAppAccordingToWeatherCondition(condition)
                }
            }
            private fun changeAppAccordingToWeatherCondition(conditions:String){
                when(conditions){
                    "Heavy Rain", "Drizzle", "Moderate", "Shower","Light Rain" ->{
                        binding.root.setBackgroundResource(R.drawable.rain_background)
                        binding.lottieAnimationView.setAnimation(R.raw.rain)
                    }
                    "Clear Sky", "Sunny", "Clear", "Smoke"->{
                        binding.root.setBackgroundResource(R.drawable.sunny_background)
                        binding.lottieAnimationView.setAnimation(R.raw.sun)
                    }
                    "Partly Clouds", "Clouds", "Overcast", "Mist"->{
                        binding.root.setBackgroundResource(R.drawable.colud_background)
                        binding.lottieAnimationView.setAnimation(R.raw.cloud)
                    }
                    "Light Snow", "Moderate Snow", "Heavy Snow", "Blizzard"->{
                        binding.root.setBackgroundResource(R.drawable.snow_background)
                        binding.lottieAnimationView.setAnimation(R.raw.snow)
                    }

                }
                binding.lottieAnimationView.playAnimation()
            }
            // DATE FUNCTION
            fun date (timestamp: Long):String{
                val sdf = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
                return  sdf.format(Date())
            }
            fun dayName (timestamp: Long):String{
                val sdf = SimpleDateFormat("EEEE", Locale.getDefault())
                return  sdf.format(Date())
            }

            override fun onFailure(call: Call<WeatherApp>, t: Throwable) {

            }

        })
    }
}