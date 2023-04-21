package com.elijah.weather.presentation.edit_location

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.elijah.weather.R
import com.elijah.weather.databinding.ItemSearchedCityBinding
import com.elijah.weather.domain.entity.City

class SearchedCityAdapterResView :
    ListAdapter<City, SearchedCityViewHolder>(SearchedCityDiffCallback) {
    var onAddCityButtonClickListener: ((City) -> Unit)? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchedCityViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_searched_city, parent, false)
        val binding = ItemSearchedCityBinding.bind(view)
        return SearchedCityViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SearchedCityViewHolder, position: Int) {
        holder.bind(getItem(position), onAddCityButtonClickListener)
    }
}