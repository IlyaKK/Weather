package com.elijah.weather.presentation.slider_weather_of_cities

data class PointOfCityItem(
    val id: Int,
    val select: Boolean = false,
    val current: Boolean
)