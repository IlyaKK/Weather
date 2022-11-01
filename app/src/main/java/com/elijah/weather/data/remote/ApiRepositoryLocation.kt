package com.elijah.weather.data.remote

import com.elijah.weather.domain.entity.Location

interface ApiRepositoryLocation {
    suspend fun getAccessLocationsByName(cityName: String): List<Location>
    suspend fun getLocationByCoordinates(
        longitude: Double,
        latitude: Double,
        added: Boolean,
        current: Boolean
    ): Location
}