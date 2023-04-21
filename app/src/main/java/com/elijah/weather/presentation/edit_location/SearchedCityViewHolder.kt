package com.elijah.weather.presentation.edit_location

import androidx.recyclerview.widget.RecyclerView
import com.elijah.weather.databinding.ItemSearchedCityBinding
import com.elijah.weather.domain.entity.City

class SearchedCityViewHolder(private val binding: ItemSearchedCityBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(item: City, onAddCityButtonClickListener: ((City) -> Unit)?) {
        with(binding) {
            cityNameTv.text = item.name
            stateCityTv.text = item.state
            countryCityTv.text = item.country
            addCityBtn.setOnClickListener {
                onAddCityButtonClickListener?.invoke(item)
            }
        }
    }
}