package com.elijah.weather.domain.entity

data class Weather(
    val main: String,
    val description: String,
    val icon: String
)

data class HourlyWeather(
    val id: Long,
    val cityName: String,
    val hourTime: String,
    val temperature: String,
    val probabilityOfPrecipitation: String,
    val weather: Weather
)

data class DailyWeather(
    val id: Long,
    val dayDate: String,
    val temperatureMin: String,
    val temperatureMax: String,
    val probabilityOfPrecipitation: String,
    val weather: Weather
)
