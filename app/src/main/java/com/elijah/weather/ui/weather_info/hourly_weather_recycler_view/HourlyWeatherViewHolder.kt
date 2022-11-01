package com.elijah.weather.ui.weather_info.hourly_weather_recycler_view

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.elijah.weather.R
import com.elijah.weather.databinding.ItemHourWeatherBinding
import com.elijah.weather.domain.entity.HourlyWeather

class HourlyWeatherViewHolder(private val binding: ItemHourWeatherBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(item: HourlyWeather, position: Int) {
        if (position == 0) {
            binding.titleHourTv.text = "Сейчас"
        } else {
            binding.titleHourTv.text = item.hourTime
        }

        when (item.weather.icon) {
            "sun" -> binding.imageWeatherIv.setImageResource(R.drawable.sun)
            "moon" -> binding.imageWeatherIv.setImageResource(R.drawable.moon)
            "snow" -> binding.imageWeatherIv.setImageResource(R.drawable.snow)
            "thunderstorm" -> binding.imageWeatherIv.setImageResource(R.drawable.thunderstorm)
            "shower_rain" -> binding.imageWeatherIv.setImageResource(R.drawable.shower_rain)
            "scattered_clouds" -> binding.imageWeatherIv.setImageResource(R.drawable.scattered_clouds)
            "few_clouds_d" -> binding.imageWeatherIv.setImageResource(R.drawable.few_clouds_d)
            "few_clouds_n" -> binding.imageWeatherIv.setImageResource(R.drawable.few_clouds_n)
            "broken_clouds" -> binding.imageWeatherIv.setImageResource(R.drawable.broken_clouds)
            "rain_d" -> binding.imageWeatherIv.setImageResource(R.drawable.rain_d)
            "rain_n" -> binding.imageWeatherIv.setImageResource(R.drawable.rain_n)
            "mist" -> binding.imageWeatherIv.setImageResource(R.drawable.mist)
        }
        if (item.probabilityOfPrecipitation.isNotEmpty()) {
            binding.probabilityPrecipitationTv.visibility = View.VISIBLE
            binding.probabilityPrecipitationTv.text =
                item.probabilityOfPrecipitation
        } else {
            binding.probabilityPrecipitationTv.visibility = View.INVISIBLE
        }
        binding.temperatureHourTv.text = item.temperature
    }
}