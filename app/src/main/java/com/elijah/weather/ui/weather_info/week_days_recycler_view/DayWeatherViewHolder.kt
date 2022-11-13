package com.elijah.weather.ui.weather_info.week_days_recycler_view

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.elijah.weather.R
import com.elijah.weather.databinding.ItemDayWeatherBinding
import com.elijah.weather.domain.entity.DailyWeather

class DayWeatherViewHolder(private val binding: ItemDayWeatherBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(item: DailyWeather, position: Int) {
        if (position == 0) {
            binding.titleDayNameTv.text = "Сегодня"
        } else {
            binding.titleDayNameTv.text = item.dayDate
        }

        when (item.weather.icon) {
            "sun" -> binding.imageWeatherDayIv.setImageResource(R.drawable.sun)
            "moon" -> binding.imageWeatherDayIv.setImageResource(R.drawable.moon)
            "snow" -> binding.imageWeatherDayIv.setImageResource(R.drawable.snow)
            "thunderstorm" -> binding.imageWeatherDayIv.setImageResource(R.drawable.thunderstorm)
            "shower_rain" -> binding.imageWeatherDayIv.setImageResource(R.drawable.shower_rain)
            "scattered_clouds" -> binding.imageWeatherDayIv.setImageResource(R.drawable.scattered_clouds)
            "few_clouds_d" -> binding.imageWeatherDayIv.setImageResource(R.drawable.few_clouds_d)
            "few_clouds_n" -> binding.imageWeatherDayIv.setImageResource(R.drawable.few_clouds_n)
            "broken_clouds" -> binding.imageWeatherDayIv.setImageResource(R.drawable.broken_clouds)
            "rain_d" -> binding.imageWeatherDayIv.setImageResource(R.drawable.rain_d)
            "rain_n" -> binding.imageWeatherDayIv.setImageResource(R.drawable.rain_n)
            "mist" -> binding.imageWeatherDayIv.setImageResource(R.drawable.mist)
        }

        if (item.probabilityOfPrecipitation.isNotEmpty()) {
            binding.percentRainTv.visibility = View.VISIBLE
            binding.percentRainTv.text =
                item.probabilityOfPrecipitation
        } else {
            binding.percentRainTv.visibility = View.GONE
        }

        val maxTemperature = binding.root.context.getString(R.string.Maximal) + item.temperatureMax
        val minTemperature = binding.root.context.getString(R.string.Minimal) + item.temperatureMin
        binding.variableMinDayTemperatureTv.text = minTemperature
        binding.variableMaxDayTemperatureTv.text = maxTemperature
    }
}