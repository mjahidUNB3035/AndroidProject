package ca.unb.mobiledev.weatherapp

//import GraphActivity
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
import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.graphics.Color
import android.provider.CalendarContract
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.TimeZone
// Import necessary classes from MPAndroidChart library
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet


class MainActivity : AppCompatActivity() {
    var accessData=false
    // gesture
    private lateinit var gestureDetector: GestureDetector
    //Line graph new
    private var forecastList: List<ForecastData>? = null
    private var forecastData: NewWeatherApp? = null//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>ForcastData
    private lateinit var lineChart: LineChart
    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    //BOTTOM LINE
    private lateinit var forecastAdapter: MinMaxTempAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setContentView(binding.root)


        setupButtons()
        //BOTTOM KLINE
        initializeRecyclerView()

//        val btnAccessData = findViewById<Button>(R.id.btn2)
//        btnAccessData.setOnClickListener {
//            accessData = true
//            Log.d("MainActivity", "Button clicked, accessData set to: $accessData")
//            initiateNetworkRequest()
//        }

        //Line Chart initialization
        lineChart = findViewById(R.id.lineChart)
        // Initially hide the line chart
        setLineChartVisibility(false)

        //Line Graph New Win

//        val btnGraph = findViewById<Button>(R.id.btnGraph) // Correctly reference the button
//        btnGraph.setOnClickListener {
//            val intent = Intent(this, GraphActivity::class.java)
//            if (forecastList != null) {
//                intent.putExtra("forecastListKey", ArrayList(forecastList)) // Ensure it's serializable
//            }
//            startActivity(intent)
//        }

        //NEW WIN
//        val btnShowMinMaxTemp = findViewById<Button>(R.id.btnShowMinMaxTemp) //btnShowMinMaxTemp
//        btnShowMinMaxTemp.setOnClickListener {
//            val intent = Intent(this, MinMaxTempActivity::class.java)
//            if (forecastList != null) {
//                intent.putExtra("forecastListKey", ArrayList(forecastList)) // Assuming minMaxTempList is your data list
//            }
//            startActivity(intent)
//        }
       // accessData=true
        getLastKnownLocation()
       // accessData=false
        setupSearchView()
        setupLocationUpdates()
        setupCalendarButton()


//        getLastKnownLocation()
//        searchCity()
        //Gesture..............................
        gestureDetector = GestureDetector(this, GestureListener())



    }
    private inner class GestureListener : GestureDetector.SimpleOnGestureListener() {
        override fun onFling(
            e1: MotionEvent,
            e2: MotionEvent,
            velocityX: Float,
            velocityY: Float
        ): Boolean {
            if (e1 != null && e2 != null && e2.x > e1.x) {
                // Perform the same action as the button click
                val intent = Intent(this@MainActivity, GraphActivity::class.java)
                if (forecastList != null) {
                    intent.putExtra("forecastListKey", ArrayList(forecastList)) // Pass the forecast data
                }
                startActivity(intent)
                return true
            }
            return false
        }
    }


    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event?.let {
            gestureDetector.onTouchEvent(it)
        }
        return super.onTouchEvent(event)
    }

    //GRAPH HIDE METHOD
    private fun setLineChartVisibility(isVisible: Boolean) {
        lineChart.visibility = if (isVisible) View.VISIBLE else View.GONE
    }


    //BOTTOM LINE
    private fun initializeRecyclerView() {
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        forecastAdapter = MinMaxTempAdapter(emptyList())
        recyclerView.adapter = forecastAdapter
    }

    private fun setupButtons() {
        // Setup listener for btnGraph
        val btnGraph = findViewById<Button>(R.id.btnGraph)
        btnGraph.visibility = View.GONE
        btnGraph.setOnClickListener {
            val intent = Intent(this, GraphActivity::class.java)
            forecastList?.let {
                intent.putExtra("forecastListKey", ArrayList(it)) // Ensure it's serializable
            }
            startActivity(intent)
        }

        val btnShowMinMaxTemp = findViewById<Button>(R.id.btnShowMinMaxTemp)
        btnShowMinMaxTemp.visibility = View.GONE
        btnShowMinMaxTemp.setOnClickListener {
            val intent = Intent(this, MinMaxTempActivity::class.java)
            forecastList?.let {
                intent.putExtra("forecastListKey", ArrayList(it))
            }
            startActivity(intent)
        }

        // Setup listener for btnAccessData
        val btnAccessData = findViewById<Button>(R.id.btn2)
        btnAccessData.setOnClickListener {
            accessData = true
            Log.d("MainActivity", "Button clicked, accessData set to: $accessData")
            initiateNetworkRequest()
        }
    }
    //line graph
    //ForecastData
    private fun setupLineChart(forecastList: List<ForecastData>) {
        val entries = ArrayList<Entry>()
        forecastList.forEachIndexed { index, forecastItem ->
            entries.add(Entry(index.toFloat(), forecastItem.main.temp.toFloat()))
        }
        if (entries.isNotEmpty()) {
            Log.d("MainActivity", "Data set inserted with size: ${entries.size}")
        } else {
            Log.d("MainActivity", "Data set is empty")
        }
        val dataSet = LineDataSet(entries, "Temperature")
        // Further dataSet configuration
        dataSet.lineWidth = 2.5f
        dataSet.color = Color.BLUE
        dataSet.setCircleColor(Color.RED)
        dataSet.circleRadius = 5f

        val lineData = LineData(dataSet)
        binding.lineChart.data = lineData
        binding.lineChart.invalidate() // Refreshes the chart

    }

     fun onResponse(call: Call<NewWeatherApp>, response: Response<NewWeatherApp>) {
        val responseBody = response.body()
        responseBody?.let {
            setupLineChart(it.list)
        }
    }


    private fun initiateNetworkRequest() {

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            // Permissions are granted, you can fetch the last known location
            Log.d("TAG", "PERMISIION DONE ON_CREATE 1")
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.WRITE_CALENDAR),
                REQUEST_CALENDAR_WRITE_PERMISSION
            )

        } else {
            // Permissions are not granted, request them
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_LOCATION_PERMISSION
            )
        }

//        searchCity()
//        getLastKnownLocation()
        setupSearchView()
        setupLocationUpdates()
        setupCalendarButton()

    }

    //Calendar Request
override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    when (requestCode) {
        REQUEST_LOCATION_PERMISSION -> {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Handle the case where location permission is granted
                Log.d("MainActivity", "Calendar write permission granted")
                getLastKnownLocation()
            } else {
                // Handle the case where location permission is denied
            }
        }
        REQUEST_CALENDAR_READ_PERMISSION -> {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Handle the case where calendar read permission is granted
            } else {
                // Handle the case where calendar read permission is denied
            }
        }
    }
}




    //Calendar
    fun addWeatherToCalendar(context: Context, weatherInfo: String, date: Long) {
        val values = ContentValues().apply {
            if(accessData){
                put(CalendarContract.Events.DTSTART, date)
                put(CalendarContract.Events.DTEND, date + 60 * 60 * 1000) // For 1 hour duration
                put(CalendarContract.Events.TITLE, "Weather Forecast")
               // put(CalendarContract.Events.TITLE, "Weather Forecast)
                put(CalendarContract.Events.DESCRIPTION, weatherInfo)
                put(CalendarContract.Events.CALENDAR_ID, getPrimaryCalendarId(context))
                put(CalendarContract.Events.EVENT_TIMEZONE, TimeZone.getDefault().id)
                Log.d("MainActivity", "Button clicked, ------ADDED to Calendar")

            }
        }







        // Check for calendar write permissions
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_CALENDAR) == PackageManager.PERMISSION_GRANTED) {
            val insertUri = context.contentResolver.insert(CalendarContract.Events.CONTENT_URI, values)
            Log.d("MainActivity","CalendarLog\", \"Event added to calendar, Uri: $insertUri, Info: $weatherInfo")
            if (insertUri != null) {
                //context.contentResolver.insert(CalendarContract.Events.CONTENT_URI, values)
                Log.d("TAG", "Event added to calendar, Uri: $insertUri")
            } else {
                Log.e("TAG", "Failed to add event to calendar")
            }

        } else {
            // Request permission from the user
            Log.d("TAG", "NOT GRANTED")
        }
    }

    companion object {
        private const val REQUEST_LOCATION_PERMISSION = 1 // Existing constant
        private const val REQUEST_CALENDAR_READ_PERMISSION = 2
        private const val REQUEST_CALENDAR_WRITE_PERMISSION = 3
    }


    @SuppressLint("Range")
    fun getPrimaryCalendarId(context: Context): Long {
        val projection = arrayOf(CalendarContract.Calendars._ID, CalendarContract.Calendars.IS_PRIMARY)
        var calendarId: Long = -1

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CALENDAR) == PackageManager.PERMISSION_GRANTED) {
            val cursor = context.contentResolver.query(
                CalendarContract.Calendars.CONTENT_URI,
                projection,
                null,
                null,
                null
            )

            if (cursor?.moveToFirst() == true) {
                do {
                    val isPrimary = cursor.getInt(cursor.getColumnIndex(CalendarContract.Calendars.IS_PRIMARY)) == 1
                    if (isPrimary) {
                        calendarId = cursor.getLong(cursor.getColumnIndex(CalendarContract.Calendars._ID))
                        Log.d("TAG", "Primary Calendar ID: $calendarId")
                        break // Break if primary calendar is found
                    }

                    // If no primary calendar, use the first calendar found
                    if (calendarId == -1L) {
                        calendarId = cursor.getLong(cursor.getColumnIndex(CalendarContract.Calendars._ID))
                    }
                } while (cursor.moveToNext())
            }

            cursor?.close()
        } else {
            // Request permission from the user
            ActivityCompat.requestPermissions(context as Activity, arrayOf(Manifest.permission.READ_CALENDAR), REQUEST_CALENDAR_READ_PERMISSION)
        }

        if (calendarId == -1L) {
            Log.d("TAG", "No valid calendar ID found") // Log an error if no valid calendar ID is found
        }
        Log.d("TAG", "ID Returned")
        return calendarId
    }

    //BOTTOM LINE
    private fun processForecastList(forecastList: List<ForecastData>): List<MinMaxTempData> {
        // Get the current date
        val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        // Group the forecast data by date
        val groupedByDate = forecastList.groupBy {
            // Convert the timestamp to a formatted date string
            SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date(it.dt.toLong() * 1000))
        }.filterKeys { it != currentDate }

        // Create a list to hold the processed data
        val minMaxTempList = mutableListOf<MinMaxTempData>()

        // Process each group
        for ((date, dailyForecasts) in groupedByDate) {
            val dailyTemps = dailyForecasts.map { it.main.temp }
            val dailyHumidities = dailyForecasts.map { it.main.humidity }

            val maxTemp = dailyTemps.maxOrNull() ?: 0.0
            val minTemp = dailyTemps.minOrNull() ?: 0.0
            val averageHumidity = if (dailyHumidities.isNotEmpty()) dailyHumidities.average().toInt() else 0

            // Convert maxTemp and minTemp to integers
            val maxTempInt = maxTemp.toInt()
            val minTempInt = minTemp.toInt()

            // Add the processed data to the list
            minMaxTempList.add(MinMaxTempData(date, maxTempInt, minTempInt, averageHumidity))
        }

        return minMaxTempList
    }


    //Current Location

//    private fun getLastKnownLocation() {
//        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
//
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//            val locationResult: Task<Location> = fusedLocationClient.lastLocation
//            locationResult.addOnSuccessListener { location ->
//                location?.let {
//                    CoroutineScope(Dispatchers.Main).launch {
//                        val cityName = getCityName(it.latitude, it.longitude)
//                        cityName?.let { name ->
//                            Log.d("TAG", "MOVING TO FETCH WEATHER DATA")
//                            fetchWeatherData(name)
//                            fetchFiveDayForecast(name)
//                        }
//                    }
//                }
//            }.addOnFailureListener {
//                // Handle the case where you don't get the location
//            }
//        } else {
//            // Request permissions from the user
//            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_LOCATION_PERMISSION)
//        }
//    }
    private fun getLastKnownLocation() {
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            val locationResult: Task<Location> = fusedLocationClient.lastLocation
            locationResult.addOnSuccessListener { location ->
                location?.let {
                    CoroutineScope(Dispatchers.Main).launch {
                        val cityName = getCityName(it.latitude, it.longitude)
                        cityName?.let { name ->
                            Log.d("TAG", "MOVING TO FETCH WEATHER DATA")
                            fetchWeatherData(name)
                            fetchFiveDayForecast(name) // Fetch forecast data after obtaining the city name
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



    //Fetch7DAY
    private fun fetchFiveDayForecast(city: String) {
        Log.d("MainActivity", "Before network request, accessData: $accessData")
        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://api.openweathermap.org/data/2.5/")
            .build().create(ApiInterface::class.java)

        val response = retrofit.getFiveDayWeatherData(city, "a172f58aec61e7a63e67507b5c61c057", "metric")
        response.enqueue(object : Callback<NewWeatherApp> {

            override fun onResponse(call: Call<NewWeatherApp>, response: Response<NewWeatherApp>) {
                Log.d("MainActivity", "Inside onResponse, accessData: $accessData")
                val forecast = response.body()
                forecast?.let {
                    forecastList = it.list // Store the fetched data

                    // Assuming 'dt' is in seconds and of type Long
                    val firstDayForecasts = it.list.groupBy {
                        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date(it.dt * 1000L))
                    }.values.firstOrNull()

                    val maxTemp = firstDayForecasts?.maxOfOrNull { it.main.temp_max } ?: 0.0
                    val minTemp = firstDayForecasts?.minOfOrNull { it.main.temp_min } ?: 0.0

                    val maxTempInt = maxTemp.toInt()
                    val minTempInt = minTemp.toInt()

                    // Update UI
                  //  binding.maxTemp.text = "Max Temp: $maxTempInt°C"
                  //  binding.minTemp.text = "Min Temp: $minTempInt°C"

//                    binding.maxTemp.text = "Max Temp: $maxTemp°C"
//                    binding.minTemp.text = "Min Temp: $minTemp°C"

                    updateRecyclerView(it.list)

                    if (accessData) {
                       // addForecastToCalendar(it) //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
                        Log.d("MainActivity", "btn pressed")
                       // accessData = false // Resetting the flag
                        //*****Pass Dta for Line hart
                        Log.d("MainActivity", "****DATA Id Passes TO Line Chart***")
                        setupLineChart(it.list)
                    }
                }

            }

            override fun onFailure(call: Call<NewWeatherApp>, t: Throwable) {
                // Handle failure
                Log.d("MainActivity", "Inside onFailure, accessData: $accessData")
            }
        })
    }
    //BOTTOM LINE
    private fun updateRecyclerView(forecastList: List<ForecastData>) {
        val processedList = processForecastList(forecastList)
        forecastAdapter.updateData(processedList)
    }

//    private fun addForecastToCalendar(forecast: NewWeatherApp) {
//        forecast.list.forEach { forecastItem ->
//            val timestamp = convertDateToTimestamp(forecastItem.dt_txt)
//            val weatherInfo = "Forecast: ${forecastItem.weather[0].main}, Temp: ${forecastItem.main.temp}°C"
//            Log.d("MainActivity", "Button clicked, addForcastIDToCalendar")
//            addWeatherToCalendar(this, weatherInfo, timestamp)  //<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
//           // Log.d("MainActivity", "Button clicked, addForcastIDToCalendar")
//        }
//    }
private fun addForecastToCalendar(forecastList: List<ForecastData>?) {
    forecastList?.forEach { forecastItem ->
        val timestamp = convertDateToTimestamp(forecastItem.dt_txt)
       // val weatherInfo = "Forecast: ${forecastItem.weather[0].main}, Temp: ${forecastItem.main.temp}°C"
        Log.d("MainActivity", "Button clicked, addForcastIDToCalendar")
        //New Data
        val weatherInfo = StringBuilder()
        weatherInfo.append("Temperature: ${forecastItem.main.temp}°C, \n")
        weatherInfo.append("Condition: ${forecastItem.weather[0].main}, \n")
        weatherInfo.append("Humidity: ${forecastItem.main.humidity}%, \n")
        weatherInfo.append("Wind Speed: ${forecastItem.wind.speed} m/s, \n")
       // weatherInfo.append("Sunrise: ${convertUnixTimeToTimeString(forecastItem)}, ")
       // weatherInfo.append("Sunset: ${convertUnixTimeToTimeString(forecastItem.sys.sunset)}")
        addWeatherToCalendar(this, weatherInfo.toString(), timestamp )
    }
}



    private fun convertDateToTimestamp(dateStr: String): Long {
        val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        return try {
            val date = format.parse(dateStr)
            date?.time ?: 0
        } catch (e: Exception) {
            Log.e("MainActivity", "Error in parsing date", e)
            0
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
    private var lastSearchedCity: String? = null
//    private fun setupSearchView() {
//        val searchView = binding.searchView
//        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
//            override fun onQueryTextSubmit(query: String?): Boolean {
//                if (query != null) {
//                    lastSearchedCity = query
//                    fetchWeatherData(query)
//                }
//                return true
//            }
//
//            override fun onQueryTextChange(newText: String?): Boolean = true
//        })
//    }

    private fun setupSearchView() {
        val searchView = binding.searchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    lastSearchedCity = query
                    accessData = true  // Set accessData to true when a city is searched
                    fetchWeatherData(query)
                    Log.d("MainActivity", "**************")
                    fetchFiveDayForecast(query)
                    //Line Graph
                    // Hide the keyboard
                    val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    inputMethodManager.hideSoftInputFromWindow(searchView.windowToken, 0)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean = true
        })
    }
    private fun setupLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            val fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                location?.let {
                    CoroutineScope(Dispatchers.Main).launch {
                        val cityName = getCityName(it.latitude, it.longitude)
                        cityName?.let { name ->
                            if (lastSearchedCity == null) {
                                fetchWeatherData(name)
                            }
                        }
                    }
                }
            }
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_LOCATION_PERMISSION)
        }
    }

//    private fun setupCalendarButton() {
//        val btnAccessData = findViewById<Button>(R.id.btn2)
//        btnAccessData.setOnClickListener {
//            accessData = true
//            Log.d("MainActivity", "Calendar button clicked")
//            // Trigger the data fetch again based on last search or current location
//            lastSearchedCity?.let { fetchWeatherData(it) } ?: getLastKnownLocation()
//        }
//    }
private fun setupCalendarButton() {
    val btnAccessData = findViewById<Button>(R.id.btn2)
    btnAccessData.setOnClickListener {
        accessData = true
        Log.d("MainActivity", "Calendar button clicked")
        // Trigger the data fetch again based on last search or current location
        lastSearchedCity?.let { fetchWeatherData(it) } ?: getLastKnownLocation()

        // Move the calendar event code here
        val weatherInfo = "Temperature: ${binding.temp.text}°C, Condition: ${binding.condition.text}"
        val eventDate = System.currentTimeMillis()
        Log.d("MainActivity", "ADD TO CALENDAR CALLED")
        addWeatherToCalendar(this@MainActivity, weatherInfo, eventDate)

        // Add the following line here to add forecast information to the calendar
        // Check if forecast data is available and add it to the calendar
        forecastList?.let { addForecastToCalendar(it) }
    }
}


    fun convertUnixTimeToTimeString(unixTime: Long): String {
        val date = Date(unixTime * 1000L) // Convert seconds to milliseconds
        val format = SimpleDateFormat("hh:mm a", Locale.getDefault()) // e.g., "06:30 PM"
        format.timeZone = TimeZone.getDefault() // Convert to local time zone
        return format.format(date)
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
                    val temperature = responseBody.main.temp.toInt().toString()
                    val humidity = responseBody.main.humidity
                    val windSpeed = responseBody.wind.speed
                    val sunRise = responseBody.sys.sunrise
                    val sunSet = responseBody.sys.sunset
                    // Assume sunRise and sunSet are Unix timestamp values you received
                    val sunRiseTime = convertUnixTimeToTimeString(sunRise.toLong())
                    val sunSetTime = convertUnixTimeToTimeString(sunSet.toLong())
                    val seaLevel = responseBody.main.pressure
                    val maxTemp = responseBody.main.temp_max.toInt()
                    val minTemp = responseBody.main.temp_min.toInt()
                    val condition = responseBody.weather.firstOrNull()?.main?:"unknown"
                   // Log.d("TAG", "onResponse: $temperature") //temp is temperature
                    binding.temp.text = "$temperature°C"
                    binding.weather.text = condition
                    binding.maxTemp.text = "Max Temp: $maxTemp"
                    binding.minTemp.text = "Min Temp: $minTemp"
                    binding.humidity.text = "Humidity: $humidity %"
                    binding.wind.text = "Max Temp: $windSpeed"
                    binding.sunrise.text = "$sunRiseTime"
                    binding.sunset.text = "$sunSetTime"
                    binding.sea.text = "$seaLevel hpma"
                    binding.condition.text = condition
                    binding.day.text = dayName(System.currentTimeMillis())
                    binding.date.text = date(System.currentTimeMillis())
                     binding.cityName.text = "$city"


                    // Adding weather information to the calendar

                        val weatherInfo = "Temperature: ${responseBody.main.temp}°C, Condition: ${responseBody.weather.firstOrNull()?.main ?: "unknown"}"

                        // Using the current system time for the calendar event date
                        val eventDate = System.currentTimeMillis()

                        // Adding weather information to the calendar
                        Log.d("MainActivity", "ADD TO CALENDAR CALLED")
                       // addWeatherToCalendar(this@MainActivity, weatherInfo, eventDate)

                    //setupLineChart(responseBody)

                    changeAppAccordingToWeatherCondition(condition, temperature)
                }
            }
            private fun changeAppAccordingToWeatherCondition(conditions:String, temp:String){
                when(conditions){
                    "Heavy Rain", "Drizzle", "Moderate", "Shower","Light Rain" ->{
                        binding.root.setBackgroundResource(R.drawable.rain_background)
                        binding.lottieAnimationView.setAnimation(R.raw.rain)
                    }
                    "Clear Sky", "Sunny",  "Smoke", "Clear"->{
                        binding.root.setBackgroundResource(R.drawable.sunny_background)
                        binding.lottieAnimationView.setAnimation(R.raw.sun)
                    }
                    "Partly Clouds", "Clouds", "Overcast", "Mist"->{
                        binding.root.setBackgroundResource(R.drawable.colud_background)
                        binding.lottieAnimationView.setAnimation(R.raw.cloud)
                    }
                    "Light Snow", "Moderate Snow", "Heavy Snow", "Blizzard", "Snow"->{
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
