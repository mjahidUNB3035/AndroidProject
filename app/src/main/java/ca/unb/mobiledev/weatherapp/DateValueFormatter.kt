package ca.unb.mobiledev.weatherapp

import com.github.mikephil.charting.formatter.ValueFormatter
import java.text.SimpleDateFormat
import java.util.*

class DateValueFormatter(private val dates: List<String>) : ValueFormatter() {
    private val dateFormat = SimpleDateFormat("dd MMM", Locale.getDefault())

    override fun getFormattedValue(value: Float): String {
        return dates.getOrNull(value.toInt()) ?: ""
    }
}
