package com.elijah.weather.data.network.model

data class WeatherContainerDto(
    val lat: Double?,
    val lon: Double?,
    val timezone: String?,
    val timezone_offset: Long?,
    val hourly: List<WeatherHourDto>,
    val daily: List<WeatherDayDto>
)
