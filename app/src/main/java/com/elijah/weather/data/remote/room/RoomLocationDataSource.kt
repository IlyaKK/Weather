package com.elijah.weather.data.remote.room

import com.elijah.weather.data.remote.DatabaseRepositoryLocation
import com.elijah.weather.data.remote.room.entity.LocationDatabase
import com.elijah.weather.domain.entity.Location
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class RoomLocationDataSource(
    private val databaseService: RoomDatabaseService
) : DatabaseRepositoryLocation {
    override suspend fun getAccessLocationsByName(cityName: String): List<Location> {
        return databaseService.locationDao().getCityByName(cityName).map { locationDatabase ->
            Location(
                latitude = locationDatabase.latitude ?: 0.0,
                longitude = locationDatabase.longitude ?: 0.0,
                cityName = locationDatabase.cityName ?: "",
                country = locationDatabase.country ?: "",
                added = true,
                current = locationDatabase.currentCity
            )
        }

    }

    override suspend fun getAllLocations(): Flow<List<Location>> {
        return flow {
            databaseService.locationDao().getAllLocations().collect {
                emit(it.map { locationDatabase ->
                    Location(
                        latitude = locationDatabase.latitude ?: 0.0,
                        longitude = locationDatabase.longitude ?: 0.0,
                        cityName = locationDatabase.cityName ?: "",
                        added = true,
                        current = locationDatabase.currentCity
                    )
                })
            }
        }
    }

    override suspend fun addLocation(location: Location, gpsLocation: Boolean) {
        if (gpsLocation) {
            val dbCurrentLocation = databaseService.locationDao().getCurrentLocation()
            dbCurrentLocation?.let {
                databaseService.locationDao()
                    .deleteLocation(dbCurrentLocation)
            }
        }

        databaseService.locationDao().insertLocation(
            LocationDatabase(
                cityName = location.cityName,
                latitude = location.latitude,
                longitude = location.longitude,
                currentCity = gpsLocation,
                country = location.country
            )
        )
    }

    override suspend fun deleteLocation(location: Location) {
        databaseService.locationDao().deleteLocationByNameCity(location.cityName)
    }
}