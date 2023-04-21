package com.elijah.weather.presentation.weather_info

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.elijah.weather.R
import com.elijah.weather.databinding.ItemHourWeatherBinding
import com.elijah.weather.domain.entity.HourlyWeather
import com.squareup.picasso.Picasso

class HourlyWeatherViewHolder(private val binding: ItemHourWeatherBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(item: HourlyWeather, position: Int) {
        if (position == 0) {
            binding.titleHourTv.text = binding.root.context.getString(R.string.now)
        } else {
            binding.titleHourTv.text = item.hourTime
        }

        if (item.probabilityOfPrecipitation.isNotEmpty()) {
            binding.probabilityPrecipitationTv.visibility = View.VISIBLE
            binding.probabilityPrecipitationTv.text =
                item.probabilityOfPrecipitation
        } else {
            binding.probabilityPrecipitationTv.visibility = View.INVISIBLE
        }
        Picasso.get().load(item.imageUrl).into(binding.imageWeatherIv)
        binding.temperatureHourTv.text = item.temperature
    }
}