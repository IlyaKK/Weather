package com.elijah.weather.presentation.weather_info

import androidx.recyclerview.widget.DiffUtil
import com.elijah.weather.domain.entity.DailyWeather

object DayWeatherDiffCallback : DiffUtil.ItemCallback<DailyWeather>() {
    override fun areItemsTheSame(
        oldItem: DailyWeather,
        newItem: DailyWeather
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: DailyWeather,
        newItem: DailyWeather
    ): Boolean {
        return oldItem == newItem
    }
}