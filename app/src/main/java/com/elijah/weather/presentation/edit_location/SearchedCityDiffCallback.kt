package com.elijah.weather.presentation.edit_location

import androidx.recyclerview.widget.DiffUtil
import com.elijah.weather.domain.entity.City

object SearchedCityDiffCallback : DiffUtil.ItemCallback<City>() {
    override fun areItemsTheSame(
        oldItem: City,
        newItem: City
    ): Boolean {
        return oldItem.name == newItem.name
    }

    override fun areContentsTheSame(
        oldItem: City,
        newItem: City
    ): Boolean {
        return oldItem == newItem
    }
}