package com.elijah.weather.domain

import com.elijah.weather.domain.entity.DailyWeather
import com.elijah.weather.domain.entity.HourlyWeather
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {
    fun getHourlyWeather(latitude: Double, longitude: Double, units: String, language: String):Flow<List<HourlyWeather>>
    fun getDailyWeather(latitude: Double, longitude: Double, units: String, language: String):Flow<List<DailyWeather>>
}