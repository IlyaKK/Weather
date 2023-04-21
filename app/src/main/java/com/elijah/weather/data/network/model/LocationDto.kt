package com.elijah.weather.data.network.model

data class LocationDto(
    val name: String?,
    val local_names: LocalNamesCityDto?,
    val lat: Double?,
    val lon: Double?,
    val country: String?,
    val state: String?
)