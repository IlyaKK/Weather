package com.elijah.weather.ui.search_location.searched_city_recycler_view

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.elijah.weather.databinding.ItemAccessCityesBinding
import com.elijah.weather.databinding.ItemSearchedCityBinding
import com.elijah.weather.domain.entity.Location

class SearchedCityViewHolder(private val binding: ItemSearchedCityBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(item: Location, listener: SearchedCityAdapterResView.OnAddCityButtonListener) {
        binding.cityNameTv.text = item.cityName
        binding.countryCityTv.text = item.country
        if (item.state.isNotEmpty()) {
            binding.stateCityTv.text = item.state
        }
        if (item.added) {
            binding.addCityIv.visibility = View.GONE
        } else {
            binding.addCityIv.visibility = View.VISIBLE
        }
        binding.addCityIv.setOnClickListener {
            listener.addNewLocation(location = item)
        }
    }

}