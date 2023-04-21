package com.elijah.weather.presentation.weather_info

import androidx.recyclerview.widget.DiffUtil
import com.elijah.weather.domain.entity.HourlyWeather

object HourlyWeatherDiffCallback : DiffUtil.ItemCallback<HourlyWeather>() {
    override fun areItemsTheSame(
        oldItem: HourlyWeather,
        newItem: HourlyWeather
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: HourlyWeather,
        newItem: HourlyWeather
    ): Boolean {
        return oldItem == newItem
    }
}