package com.elijah.weather.ui.weather_info.week_days_recycler_view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
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

object DayWeatherDiffCallback : DiffUtil.ItemCallback<DailyWeather>() {
    override fun areItemsTheSame(
        oldItem: DailyWeather,
        newItem: DailyWeather
    ): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(
        oldItem: DailyWeather,
        newItem: DailyWeather
    ): Boolean {
        return oldItem.id == newItem.id
    }
}