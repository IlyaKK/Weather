package com.elijah.weather.data.remote

import com.elijah.weather.domain.entity.Location
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class RemoteLocationDataSourceImpl(
    private val internetDataSource: ApiRepositoryLocation,
    private val databaseSource: DatabaseRepositoryLocation
) : RemoteLocationDataSource {
    override suspend fun getAccessLocationsByName(cityName: String): List<Location> {
        val listAccess = mutableListOf<Location>()
        val databaseLocations = databaseSource.getAccessLocationsByName(cityName)
        val internetLocations = internetDataSource.getAccessLocationsByName(cityName)
        if (databaseLocations.isNotEmpty()) {
            listAccess.addAll(databaseLocations)
        }
        if (internetLocations.isNotEmpty()) {
            listAccess.addAll(internetLocations)
        }
        return listAccess
    }

    override suspend fun getAllLocations(): Flow<List<Location>> {
        return flow {
            databaseSource.getAllLocations().collect {
                emit(it)
            }
        }
    }

    override suspend fun addLocation(location: Location, gpsLocation: Boolean) {
        if (gpsLocation) {
            val newLocation =
                internetDataSource.getLocationByCoordinates(
                    location.longitude,
                    location.latitude,
                    location.added,
                    location.current
                )
            databaseSource.addLocation(newLocation, gpsLocation)
        } else {
            databaseSource.addLocation(location, gpsLocation)
        }
    }

    override suspend fun deleteLocation(location: Location) {
        databaseSource.deleteLocation(location)
    }
}