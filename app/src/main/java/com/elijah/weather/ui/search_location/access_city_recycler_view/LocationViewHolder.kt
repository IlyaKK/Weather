package com.elijah.weather.ui.search_location.access_city_recycler_view

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.elijah.weather.databinding.ItemAccessCityesBinding
import com.elijah.weather.domain.entity.Location

class LocationViewHolder(private val binding: ItemAccessCityesBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(
        item: Location,
        listenerOnClickDeleteCityButtonListener: AccessCityAdapterResView.OnClickDeleteCityButtonListener
    ) {
        binding.accessCityName.text = item.cityName
        binding.latitudeTv.text = item.latitude.toDoubleStringFormat()
        binding.longitudeTv.text = item.longitude.toDoubleStringFormat()
        if (item.current) {
            binding.deleteCityIv.visibility = View.INVISIBLE
        } else {
            binding.deleteCityIv.visibility = View.VISIBLE
        }
        binding.deleteCityIv.setOnClickListener {
            listenerOnClickDeleteCityButtonListener.deleteCity(item)
        }
    }

    private fun Double.toDoubleStringFormat() = "%.2f".format(this)
}