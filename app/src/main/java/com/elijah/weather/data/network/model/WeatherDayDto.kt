package com.elijah.weather.data.network.model

data class WeatherDayDto(
    val dt: Long?,
    val sunrise: Long?,
    val sunset: Long?,
    val moonrise: Long?,
    val moonset: Long?,
    val moon_phase: Float?,
    val temp: DayTemperaturesDto,
    val weather: List<WeatherDescriptionDto>,
    val pop: Float?
)