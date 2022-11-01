package com.elijah.weather.data.remote

import com.elijah.weather.domain.entity.DailyWeather
import com.elijah.weather.domain.entity.HourlyWeather

interface RemoteWeatherDataSource {
    suspend fun getHourlyWeather(
        latitude: Double,
        longitude: Double,
        units: String,
        language: String
    ): List<HourlyWeather>

    suspend fun getDailyWeather(
        latitude: Double,
        longitude: Double,
        units: String,
        language: String
    ): List<DailyWeather>
}