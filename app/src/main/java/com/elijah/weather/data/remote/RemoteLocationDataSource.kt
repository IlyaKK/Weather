package com.elijah.weather.data.remote

import com.elijah.weather.domain.entity.Location
import kotlinx.coroutines.flow.Flow

interface RemoteLocationDataSource {
    suspend fun getAccessLocationsByName(cityName: String): List<Location>
    suspend fun getAllLocations(): Flow<List<Location>>
    suspend fun addLocation(location: Location, gpsLocation: Boolean)
    suspend fun deleteLocation(location: Location)
}