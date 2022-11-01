package com.elijah.weather.data

import com.elijah.weather.data.remote.RemoteLocationDataSource
import com.elijah.weather.domain.LocationRepository
import com.elijah.weather.domain.entity.Location
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class LocationRepositoryImpl(private val remoteLocationDataSource: RemoteLocationDataSource) :
    LocationRepository {

    override fun getCoordinatesByNameCity(cityName: String): Flow<List<Location>> {
        return flow { emit(remoteLocationDataSource.getAccessLocationsByName(cityName)) }
    }

    override suspend fun addLocation(location: Location, gpsLocation: Boolean) {
        remoteLocationDataSource.addLocation(location, gpsLocation)
    }

    override fun getListAddedLocations(): Flow<List<Location>> {
        return flow {
            remoteLocationDataSource.getAllLocations().collect {
                emit(it)
            }
        }
    }

    override suspend fun deleteLocation(location: Location) {
        remoteLocationDataSource.deleteLocation(location)
    }
}