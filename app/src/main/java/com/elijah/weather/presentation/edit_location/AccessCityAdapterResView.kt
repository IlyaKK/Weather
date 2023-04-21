package com.elijah.weather.presentation.edit_location

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.elijah.weather.R
import com.elijah.weather.databinding.ItemAccessCityesBinding
import com.elijah.weather.domain.entity.City

class AccessCityAdapterResView :
    ListAdapter<City, AccessCityViewHolder>(AccessCityDiffCallback) {
    var listenerOnClickDeleteCityButton: ((City) -> Unit)? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AccessCityViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_access_cityes, parent, false)
        val binding = ItemAccessCityesBinding.bind(view)
        return AccessCityViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AccessCityViewHolder, position: Int) {
        holder.bind(getItem(position), listenerOnClickDeleteCityButton)
    }
}