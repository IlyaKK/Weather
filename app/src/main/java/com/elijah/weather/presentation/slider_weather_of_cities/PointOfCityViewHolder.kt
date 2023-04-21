package com.elijah.weather.presentation.slider_weather_of_cities

import androidx.recyclerview.widget.RecyclerView
import com.elijah.weather.R
import com.elijah.weather.databinding.ItemPointerBinding

class PointOfCityViewHolder(private val binding: ItemPointerBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(item: PointOfCityItem) {
        if (item.current) {
            if (item.select) {
                binding.pointIv.setImageResource(R.drawable.ic_baseline_navigation_d)
            } else {
                binding.pointIv.setImageResource(R.drawable.ic_baseline_navigation_l)
            }
        } else {
            if (item.select) {
                binding.pointIv.setImageResource(R.drawable.ic_baseline_circle_d)
            } else {
                binding.pointIv.setImageResource(R.drawable.ic_baseline_circle_l)
            }
        }
    }
}