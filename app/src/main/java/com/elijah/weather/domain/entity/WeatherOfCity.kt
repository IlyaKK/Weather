package com.elijah.weather.domain.entity

data class WeatherOfCity(
    val daysWeather: List<DailyWeather>,
    val hoursWeather: List<HourlyWeather>
)