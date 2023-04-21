package com.elijah.weather.domain.entity

data class HourlyWeather(
    val id: Int = UNDEFINED_ID,
    val hourTime: String,
    val imageUrl: String,
    val probabilityOfPrecipitation: String,
    val temperature: String,
    val weatherDescription: String
) {
    companion object {
        private const val UNDEFINED_ID = 0
    }
}
