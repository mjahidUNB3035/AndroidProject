package ca.unb.mobiledev.weatherapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class MinMaxTempActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MinMaxTempAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.min_max_temp)

        recyclerView = findViewById(R.id.recyclerViewMinMaxTemp)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = MinMaxTempAdapter(emptyList())
        recyclerView.adapter = adapter

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewMinMaxTemp)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        val minMaxTempList = intent.getSerializableExtra("forecastListKey") as? List<MinMaxTempData> ?: emptyList()

        loadData()

    }

    private fun loadData() {
        // Get the forecast data and update the adapter
        val forecastList = intent.getSerializableExtra("forecastListKey") as? List<ForecastData>
        forecastList?.let {
            // Process the list to extract max and min temperatures for each day
            val minMaxTempList = processForecastList(it)
            adapter.updateData(minMaxTempList)
        }
    }

    private fun processForecastList(forecastList: List<ForecastData>): List<MinMaxTempData> {
        // Group the forecast data by date
        val groupedByDate = forecastList.groupBy { SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(
            Date(it.dt.toLong() * 1000)
        ) }

        // Create a list to hold the processed data
        val minMaxTempList = mutableListOf<MinMaxTempData>()

        // Process each group
        for ((date, dailyForecasts) in groupedByDate) {
            val dailyTemps = dailyForecasts.map { it.main.temp }
            val maxTemp = dailyTemps.maxOrNull() ?: 0.0
            val minTemp = dailyTemps.minOrNull() ?: 0.0
            val averageHumidity = dailyForecasts.map { it.main.humidity }.average().toInt()
            // Add the processed data to the list
           // minMaxTempList.add(MinMaxTempData(date, maxTemp.toFloat(), minTemp.toFloat()),averageHumidity)
            minMaxTempList.add(MinMaxTempData(date, maxTemp.toInt(), minTemp.toInt(), averageHumidity))

        }

        return minMaxTempList
    }

}


