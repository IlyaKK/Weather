package com.elijah.weather.presentation.edit_location

import androidx.recyclerview.widget.DiffUtil
import com.elijah.weather.domain.entity.City

object AccessCityDiffCallback : DiffUtil.ItemCallback<City>() {
    override fun areItemsTheSame(
        oldItem: City,
        newItem: City
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: City,
        newItem: City
    ): Boolean {
        return oldItem == newItem
    }
}