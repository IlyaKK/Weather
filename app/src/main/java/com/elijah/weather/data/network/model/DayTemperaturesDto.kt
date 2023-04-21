package com.elijah.weather.data.network.model

data class DayTemperaturesDto(
    val day: Float?,
    val min: Float?,
    val max: Float?,
    val night: Float?,
    val eve: Float?,
    val morn: Float?,
    val weather: List<WeatherDescriptionDto>,
    val pop: Float?
)