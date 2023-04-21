package com.elijah.weather.presentation.weather_info

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.elijah.weather.R
import com.elijah.weather.databinding.ItemHourWeatherBinding
import com.elijah.weather.domain.entity.HourlyWeather

class HourlyDayWeatherAdapterResView :
    ListAdapter<HourlyWeather, HourlyWeatherViewHolder>(HourlyWeatherDiffCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HourlyWeatherViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_hour_weather, parent, false)
        val binding = ItemHourWeatherBinding.bind(view)
        return HourlyWeatherViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HourlyWeatherViewHolder, position: Int) {
        holder.bind(getItem(position), position)
    }
}