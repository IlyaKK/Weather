package com.elijah.weather.ui.search_location.access_city_recycler_view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.elijah.weather.R
import com.elijah.weather.databinding.ItemAccessCityesBinding
import com.elijah.weather.domain.entity.Location

class AccessCityAdapterResView :
    ListAdapter<Location, LocationViewHolder>(LocationDiffCallback) {
    private lateinit var listenerOnClickDeleteCityButtonListener: OnClickDeleteCityButtonListener
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_access_cityes, parent, false)
        val binding = ItemAccessCityesBinding.bind(view)
        return LocationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LocationViewHolder, position: Int) {
        holder.bind(getItem(position), listenerOnClickDeleteCityButtonListener)
    }

    fun setOnDeleteCityClickButton(listener: OnClickDeleteCityButtonListener) {
        this.listenerOnClickDeleteCityButtonListener = listener
    }

    interface OnClickDeleteCityButtonListener {
        fun deleteCity(location: Location)
    }
}

object LocationDiffCallback : DiffUtil.ItemCallback<Location>() {
    override fun areItemsTheSame(
        oldItem: Location,
        newItem: Location
    ): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(
        oldItem: Location,
        newItem: Location
    ): Boolean {
        return oldItem.cityName == newItem.cityName
    }
}