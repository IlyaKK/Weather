package com.elijah.weather.presentation.slider_weather_of_cities

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.elijah.weather.R
import com.elijah.weather.databinding.ItemPointerBinding

class PointOfCityAdapter :
    ListAdapter<PointOfCityItem, PointOfCityViewHolder>(PointersDiffCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PointOfCityViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_pointer, parent, false)
        val binding = ItemPointerBinding.bind(view)
        return PointOfCityViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PointOfCityViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}