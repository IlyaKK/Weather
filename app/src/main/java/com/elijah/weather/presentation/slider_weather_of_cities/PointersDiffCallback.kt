package com.elijah.weather.presentation.slider_weather_of_cities

import androidx.recyclerview.widget.DiffUtil

object PointersDiffCallback : DiffUtil.ItemCallback<PointOfCityItem>() {
    override fun areItemsTheSame(
        oldItem: PointOfCityItem,
        newItem: PointOfCityItem
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: PointOfCityItem,
        newItem: PointOfCityItem
    ): Boolean {
        return oldItem == newItem
    }
}