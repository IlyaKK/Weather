package com.elijah.weather.presentation.weather_info

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.elijah.weather.R
import com.elijah.weather.databinding.ItemDayWeatherBinding
import com.elijah.weather.domain.entity.DailyWeather

class WeekDaysWeatherAdapterResView :
    ListAdapter<DailyWeather, DayWeatherViewHolder>(DayWeatherDiffCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayWeatherViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_day_weather, parent, false)
        val binding = ItemDayWeatherBinding.bind(view)
        return DayWeatherViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DayWeatherViewHolder, position: Int) {
        holder.bind(getItem(position), position)
    }
}