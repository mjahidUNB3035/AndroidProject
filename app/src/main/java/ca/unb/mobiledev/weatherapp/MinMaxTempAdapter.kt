
package ca.unb.mobiledev.weatherapp

import ca.unb.mobiledev.weatherapp.MinMaxTempAdapter
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ca.unb.mobiledev.weatherapp.databinding.MinMaxTempItemBinding


class MinMaxTempAdapter(private var minMaxTempList: List<MinMaxTempData>) : RecyclerView.Adapter<MinMaxTempAdapter.MinMaxTempViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MinMaxTempViewHolder {
        // Use view binding to inflate the layout
        val binding = MinMaxTempItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MinMaxTempViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MinMaxTempViewHolder, position: Int) {
        val item = minMaxTempList[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return minMaxTempList.size
    }

    class MinMaxTempViewHolder(private val binding: MinMaxTempItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: MinMaxTempData) {
            // Bind data to the view
            binding.textViewDate.text = data.date
            binding.textViewMaxTemp.text = "Max: ${data.maxTemp}"
            binding.textViewMinTemp.text = "Min: ${data.minTemp}"
            binding.textViewHumidity.text = "Humidity: ${data.humidity}%"
           // binding.textViewHumidity.text = "Humidity: ${data.humidity}%"
            //binding.textViewWindSpeed.text = "Wind: ${data.windSpeed} km/h"
        }
    }

    fun updateData(newMinMaxTempList: List<MinMaxTempData>) {
        minMaxTempList = newMinMaxTempList
        notifyDataSetChanged()
    }
}
