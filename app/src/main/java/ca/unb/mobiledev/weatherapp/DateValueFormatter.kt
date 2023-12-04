package ca.unb.mobiledev.weatherapp

import com.github.mikephil.charting.formatter.ValueFormatter
import java.text.SimpleDateFormat
import java.util.*

class DateValueFormatter(private val dates: List<String>) : ValueFormatter() {
    private val dateFormat = SimpleDateFormat("dd MMM", Locale.getDefault())

    override fun getFormattedValue(value: Float): String {
        // Assuming 'value' is an index, convert it to a date string
        return dates.getOrNull(value.toInt()) ?: ""
    }
}
