package com.elijah.weather.domain

import com.elijah.weather.domain.entity.Location
import kotlinx.coroutines.flow.Flow

interface LocationRepository {
    fun getCoordinatesByNameCity(cityName: String): Flow<List<Location>>
    suspend fun addLocation(location: Location, gpsLocation: Boolean = false)
    fun getListAddedLocations(): Flow<List<Location>>
    suspend fun deleteLocation(location: Location)
}