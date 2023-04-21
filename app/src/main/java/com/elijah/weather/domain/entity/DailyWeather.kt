package com.elijah.weather.domain.entity

data class DailyWeather(
    val id: Int = UNDEFINED_ID,
    val dayDate: String,
    val imageUrl: String,
    val probabilityOfPrecipitation: String,
    val temperatureMin: String,
    val temperatureMax: String
) {
    companion object {
        private const val UNDEFINED_ID = 0
    }
}
