package com.elijah.weather.ui.search_location.searched_city_recycler_view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.elijah.weather.R
import com.elijah.weather.databinding.ItemSearchedCityBinding
import com.elijah.weather.domain.entity.Location

class SearchedCityAdapterResView :
    ListAdapter<Location, SearchedCityViewHolder>(SearchedDiffCallback) {
    private lateinit var listener: OnAddCityButtonListener
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchedCityViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_searched_city, parent, false)
        val binding = ItemSearchedCityBinding.bind(view)
        return SearchedCityViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SearchedCityViewHolder, position: Int) {
        holder.bind(getItem(position), listener)
    }

    fun setOnAddNewCityClickButton(listener: OnAddCityButtonListener) {
        this.listener = listener
    }

    interface OnAddCityButtonListener {
        fun addNewLocation(location: Location)
    }
}

object SearchedDiffCallback : DiffUtil.ItemCallback<Location>() {
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
        return (oldItem.latitude == newItem.latitude) && (oldItem.longitude == oldItem.longitude)
    }
}