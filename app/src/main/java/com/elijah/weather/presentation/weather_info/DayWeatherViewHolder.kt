package com.elijah.weather.presentation.weather_info

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.elijah.weather.R
import com.elijah.weather.databinding.ItemDayWeatherBinding
import com.elijah.weather.domain.entity.DailyWeather
import com.squareup.picasso.Picasso

class DayWeatherViewHolder(private val binding: ItemDayWeatherBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(item: DailyWeather, position: Int) {
        if (position == 0) {
            binding.titleDayNameTv.text = binding.root.context.getString(R.string.today)
        } else {
            binding.titleDayNameTv.text = item.dayDate
        }

        if (item.probabilityOfPrecipitation.isNotEmpty()) {
            binding.percentRainTv.visibility = View.VISIBLE
            binding.percentRainTv.text =
                item.probabilityOfPrecipitation
        } else {
            binding.percentRainTv.visibility = View.GONE
        }
        binding.variableMinDayTemperatureTv.text = item.temperatureMin
        binding.variableMaxDayTemperatureTv.text = item.temperatureMax

        Picasso.get().load(item.imageUrl).into(binding.imageWeatherDayIv)
    }
}