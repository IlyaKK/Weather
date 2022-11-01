package com.elijah.weather

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.elijah.weather.domain.LocationRepository
import com.elijah.weather.domain.WeatherRepository
import com.elijah.weather.ui.LocationViewModel
import com.elijah.weather.ui.weather_info.WeatherInfoViewModel

class ViewModelFactory(
    private val weatherRepository: WeatherRepository,
    private val locationRepository: LocationRepository
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {
        if (modelClass.isAssignableFrom(WeatherInfoViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return WeatherInfoViewModel(weatherRepository) as T
        } else if (modelClass.isAssignableFrom(LocationViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LocationViewModel(locationRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}