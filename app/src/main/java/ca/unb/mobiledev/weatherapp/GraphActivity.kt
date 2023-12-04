package ca.unb.mobiledev.weatherapp


import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.widget.CheckBox
import androidx.appcompat.app.AppCompatActivity
import ca.unb.mobiledev.weatherapp.ForecastData
import ca.unb.mobiledev.weatherapp.R
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet

//import ca.unb.mobiledev.weatherapp.databinding.ActivityGraphBinding
import retrofit2.Call
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Locale

class GraphActivity  : AppCompatActivity() {
    private lateinit var lineChart: LineChart
    private lateinit var gestureDetector: GestureDetector
    val humidityEntries = ArrayList<Entry>()
    //checkbox1
    private lateinit var checkboxTemperature: CheckBox
    private lateinit var checkboxHumidity: CheckBox
    private lateinit var checkboxWindSpeed: CheckBox
    private lateinit var checkboxFeelsLike: CheckBox

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_graph)

        //gesture
        gestureDetector = GestureDetector(this, object : GestureDetector.SimpleOnGestureListener() {
            override fun onFling(
                e1: MotionEvent,
                e2: MotionEvent,
                velocityX: Float,
                velocityY: Float
            ): Boolean {
                // Detect left swipe or any other gesture
                if (e1.x - e2.x > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                    finish() // Close the activity
                    return true
                }
                return super.onFling(e1, e2, velocityX, velocityY)
            }
        })


        lineChart = findViewById(R.id.lineChart)
        lineChart.setBackgroundColor(Color.parseColor("#D3D3D3"))

        val forecastList = intent.getSerializableExtra("forecastListKey") as? List<ForecastData>
        if (forecastList != null) {
            setupLineChart(forecastList)
        } else {
            Log.d("GraphActivity", "No forecast data found")
        }
    }





    private fun setupLineChart(forecastList: List<ForecastData>?) {
        val temperatureEntries = ArrayList<Entry>()
        val humidityEntries = ArrayList<Entry>()
        val windSpeedEntries = ArrayList<Entry>()
        val feelsLikeEntries = ArrayList<Entry>()


        if (forecastList != null) {
            forecastList.forEachIndexed { index, forecastItem ->
                //temperatureEntries.add(Entry(index.toFloat(), forecastItem.main.temp.toFloat()))
                //DATE CANGE __________________________________________>>
                temperatureEntries.add(Entry(index.toFloat(), forecastItem.main.temp.toFloat()))
                humidityEntries.add(Entry(index.toFloat(), forecastItem.main.humidity.toFloat()))
                windSpeedEntries.add(Entry(index.toFloat(), forecastItem.wind.speed.toFloat()))
                feelsLikeEntries.add(Entry(index.toFloat(), forecastItem.main.feels_like.toFloat()))
            }
        }

        val temperatureDataSet = LineDataSet(temperatureEntries, "Temperature")
        val humidityDataSet = LineDataSet(humidityEntries, "Humidity")
        val windSpeedDataSet = LineDataSet(windSpeedEntries, "Wind Speed")
        val feelsLikeDataSet = LineDataSet(feelsLikeEntries, "Feels Like")

        val dateList = forecastList?.map { forecastItem ->
            SimpleDateFormat("dd MMM", Locale.getDefault()).format(forecastItem.dt)
        }

//        val temperatureEntries = ArrayList<Entry>()
//        if (forecastList != null) {
//            forecastList.forEachIndexed { index, forecastItem ->
//                temperatureEntries.add(Entry(index.toFloat(), forecastItem.main.temp.toFloat()))
//            }
//        }

        // Customize the appearance of each data set as needed
        temperatureDataSet.color = Color.RED
        temperatureDataSet.setCircleColor(Color.RED)
        temperatureDataSet.circleRadius = 5f

        humidityDataSet.color = Color.BLUE
        humidityDataSet.setCircleColor(Color.BLUE)
        humidityDataSet.circleRadius = 5f

        windSpeedDataSet.color = Color.GREEN
        windSpeedDataSet.setCircleColor(Color.GREEN)
        windSpeedDataSet.circleRadius = 5f

        feelsLikeDataSet.color = Color.MAGENTA // Customize the color for "Feels Like"
        feelsLikeDataSet.setCircleColor(Color.MAGENTA)
        feelsLikeDataSet.circleRadius = 5f

        // Create a LineData object with all data sets
        val lineData = LineData(temperatureDataSet, humidityDataSet, windSpeedDataSet, feelsLikeDataSet)

        // Set the line data to the chart
        lineChart.data = lineData

        // Customize other chart settings if needed
        lineChart.isScaleXEnabled = true
        lineChart.isScaleYEnabled = true
        lineChart.setVisibleXRangeMaximum(10f)
        lineChart.moveViewToX(temperatureDataSet.entryCount.toFloat())

        // Refresh the chart
        lineChart.invalidate()

        // Set listeners for checkboxes to show/hide corresponding line graphs
        //setCheckBoxListeners()
        //checkbox2
        checkboxTemperature = findViewById(R.id.checkboxTemperature)
        checkboxHumidity = findViewById(R.id.checkboxHumidity)
        checkboxWindSpeed = findViewById(R.id.checkboxWindSpeed)
        checkboxFeelsLike = findViewById(R.id.checkboxFeelsLike)

// Set listeners for checkboxes to show/hide corresponding line graphs
        //DATE CHANGE __________________________________________>>
        val xAxisFormatter = dateList?.let { DateValueFormatter(it) }
        lineChart.xAxis.valueFormatter = xAxisFormatter

        setCheckBoxListeners()

    }
    private fun setCheckBoxListeners() {
        checkboxTemperature.setOnCheckedChangeListener { _, isChecked ->
            setDataSetVisibility(0, isChecked) // 0 corresponds to the index of the Temperature data set
        }

        checkboxHumidity.setOnCheckedChangeListener { _, isChecked ->
            setDataSetVisibility(1, isChecked) // 1 corresponds to the index of the Humidity data set
        }

        checkboxWindSpeed.setOnCheckedChangeListener { _, isChecked ->
            setDataSetVisibility(2, isChecked) // 2 corresponds to the index of the Wind Speed data set
        }

        checkboxFeelsLike.setOnCheckedChangeListener { _, isChecked ->
            setDataSetVisibility(3, isChecked) // 3 corresponds to the index of the Feels Like data set
        }
    }

    private fun setDataSetVisibility(dataSetIndex: Int, isVisible: Boolean) {
        val dataSet = lineChart.data.dataSets[dataSetIndex]
        dataSet.isVisible = isVisible
        lineChart.invalidate()
    }



//    private fun setupLineChart(forecastList: List<ForecastData>?) {
//        val temperatureEntries = ArrayList<Entry>()
//        val humidityEntries = ArrayList<Entry>()
//        val windSpeedEntries = ArrayList<Entry>()
//        val precipitationEntries = ArrayList<Entry>()
//        val pressureEntries = ArrayList<Entry>()
//
//        if (forecastList != null) {
//            forecastList.forEachIndexed { index, forecastItem ->
//                temperatureEntries.add(Entry(index.toFloat(), forecastItem.main.temp.toFloat()))
//                humidityEntries.add(Entry(index.toFloat(), forecastItem.main.humidity.toFloat()))
//                windSpeedEntries.add(Entry(index.toFloat(), forecastItem.main.sea_level.toFloat()))
//                precipitationEntries.add(Entry(index.toFloat(), forecastItem.main.pressure.toFloat()))
//                pressureEntries.add(Entry(index.toFloat(), forecastItem.main.feels_like.toFloat()))
//            }
//        }
//
//        val temperatureDataSet = LineDataSet(temperatureEntries, "Temperature")
//        val humidityDataSet = LineDataSet(humidityEntries, "Humidity")
//        val windSpeedDataSet = LineDataSet(windSpeedEntries, "Wind Speed")
//        val precipitationDataSet = LineDataSet(precipitationEntries, "Precipitation")
//        val pressureDataSet = LineDataSet(pressureEntries, "Pressure")
//
//        // Customize the data sets as needed, e.g., set colors, circle sizes, etc.
//
//        val lineData = LineData(
//            temperatureDataSet, humidityDataSet
//        )
//
//        lineChart.data = lineData
//        lineChart.isScaleXEnabled = true
//        lineChart.isScaleYEnabled = false
//        lineChart.setVisibleXRangeMaximum(10f)
//        lineChart.moveViewToX(temperatureDataSet.entryCount.toFloat())
//
//        lineChart.invalidate()
//    }


//    private fun setupLineChart(forecastList: List<ForecastData>?) {
//        val temperatureEntries = ArrayList<Entry>()
//        val humidityEntries = ArrayList<Entry>()
//
//        if (forecastList != null) {
//            forecastList.forEachIndexed { index, forecastItem ->
//                // Use the correct fields for temperature and humidity from your forecast item class
//                temperatureEntries.add(Entry(index.toFloat(), forecastItem.main.temp.toFloat()))
//                humidityEntries.add(Entry(index.toFloat(), forecastItem.main.humidity.toFloat()))
//            }
//        }
//
//        // Create LineDataSet for temperature
//        val temperatureDataSet = LineDataSet(temperatureEntries, "Temperature")
//        temperatureDataSet.lineWidth = 2.5f
//        temperatureDataSet.color = Color.BLUE
//        temperatureDataSet.setCircleColor(Color.RED)
//        temperatureDataSet.circleRadius = 5f
//
//        // Create LineDataSet for humidity
//        val humidityDataSet = LineDataSet(humidityEntries, "Humidity")
//        humidityDataSet.lineWidth = 2.5f
//        humidityDataSet.color = Color.GREEN // Change color for humidity
//        humidityDataSet.setCircleColor(Color.YELLOW) // Change circle color for humidity
//        humidityDataSet.circleRadius = 5f
//
//        // Create a LineData object with both data sets
//        val lineData = LineData(temperatureDataSet, humidityDataSet)
//
//        lineChart.data = lineData
//        lineChart.isScaleXEnabled = true
//        lineChart.isScaleYEnabled = false
//        lineChart.setVisibleXRangeMaximum(10f)
//        lineChart.moveViewToX(temperatureDataSet.entryCount.toFloat())
//
//        lineChart.invalidate()
//    }


    //    private fun setupLineChart(forecastList: List<ForecastData>?) {
//        val entries = ArrayList<Entry>()
//        if (forecastList != null) {
//            forecastList.forEachIndexed { index, forecastItem ->
//                // Use the correct temperature field from your actual forecast item class
//                entries.add(Entry(index.toFloat(), forecastItem.main.temp.toFloat()))
//            }
//        }
//        if (entries.isNotEmpty()) {
//            Log.d("MainActivity", "Data set inserted with size: ${entries.size}")
//        } else {
//            Log.d("MainActivity", "Data set is empty")
//        }
//
//        // Create LineDataSet for temperature
//        val tempDataSet = LineDataSet(entries, "Temperature")
//        // Further dataSet configuration...
//        tempDataSet.lineWidth = 2.5f
//        tempDataSet.color = Color.BLUE
//        tempDataSet.setCircleColor(Color.RED)
//        tempDataSet.circleRadius = 5f
//
//        // Create LineDataSet for humidity
//        val humidityDataSet = LineDataSet(humidityEntries, "Humidity")
//        humidityDataSet.lineWidth = 2.5f
//        humidityDataSet.color = Color.GREEN // Change color for humidity
//        humidityDataSet.setCircleColor(Color.YELLOW) // Change circle color for humidity
//        humidityDataSet.circleRadius = 5f
//
//        val lineData = LineData(tempDataSet,  humidityDataSet)
//        lineChart.data = lineData
//        // Enable horizontal scrolling
//        lineChart.isScaleXEnabled = true
//        lineChart.isScaleYEnabled = false // If you want to disable vertical scaling
//
//        // Set the maximum visible range. Adjust the value as needed.
//        lineChart.setVisibleXRangeMaximum(10f) // for example, show 10 data points at a time
//
//        // Move to the latest entry (rightmost data)
//        lineChart.moveViewToX(tempDataSet.entryCount.toFloat())
//
//
//        lineChart.invalidate() // Refreshes the chart
//    }
    fun onResponse(call: Call<NewWeatherApp>, response: Response<NewWeatherApp>) {
        val responseBody = response.body()
        responseBody?.let {
            // Assuming 'list' is the list of forecast items in your NewWeatherApp class
            setupLineChart(it.list)
        }
    }

    //gesture companion
    companion object {
        private const val SWIPE_THRESHOLD = 100
        private const val SWIPE_VELOCITY_THRESHOLD = 100
    }
    //gesture onTouchevent
    override fun onTouchEvent(event: MotionEvent): Boolean {
        gestureDetector.onTouchEvent(event)
        return super.onTouchEvent(event)
    }

}
