package com.elijah.weather.presentation.edit_location

import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.elijah.weather.databinding.ItemAccessCityesBinding
import com.elijah.weather.domain.entity.City

class AccessCityViewHolder(private val binding: ItemAccessCityesBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(
        item: City,
        listenerOnClickDeleteCityButtonListener: ((City) -> Unit)?
    ) {
        with(binding) {
            accessCityNameTv.text = item.name
            countryTv.text = item.country
            stateTv.text = item.state
            deleteCityBtn.isVisible = !item.currentCity
            deleteCityBtn.setOnClickListener {
                listenerOnClickDeleteCityButtonListener?.invoke(item)
            }
        }
    }
}