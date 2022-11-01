package com.elijah.weather.domain.entity

data class Location(
    val latitude: Double,
    val longitude: Double,
    var cityName: String = "",
    val country: String = "",
    val state: String = "",
    val added: Boolean,
    val current: Boolean
)