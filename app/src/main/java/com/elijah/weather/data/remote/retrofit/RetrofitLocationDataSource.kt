package com.elijah.weather.data.remote.retrofit

import com.elijah.weather.BuildConfig
import com.elijah.weather.data.remote.ApiRepositoryLocation
import com.elijah.weather.domain.entity.Location
import kotlinx.coroutines.flow.Flow

class RetrofitLocationDataSource(private val api: WeatherApiService) : ApiRepositoryLocation {

    override suspend fun getAccessLocationsByName(cityName: String): List<Location> {
        return api.getLocationCoordinatesByCityName(
            cityName,
            apiKey = BuildConfig.WEATHER_ONE_CALL_API_KEY
        ).map { locationApiResponse ->
            Location(
                latitude = locationApiResponse.lat ?: 0.0,
                longitude = locationApiResponse.lon ?: 0.0,
                cityName = locationApiResponse.local_names.ru
                    ?: (locationApiResponse.local_names.feature_name
                        ?: (locationApiResponse.local_names.ascii ?: (locationApiResponse.name
                            ?: ""))),
                country = locationApiResponse.country ?: "",
                state = locationApiResponse.state ?: "",
                added = false,
                current = false
            )
        }
    }
    
    override suspend fun getLocationByCoordinates(
        longitude: Double,
        latitude: Double,
        added: Boolean,
        current: Boolean
    ): Location {
        val locationResponse = api.getNameCityByCoordinates(
            latitude,
            longitude,
            apiKey = BuildConfig.WEATHER_ONE_CALL_API_KEY
        )
        return Location(
            latitude = locationResponse[0].lat ?: 0.0,
            longitude = locationResponse[0].lon ?: 0.0,
            cityName = locationResponse[0].local_names.ru
                ?: (locationResponse[0].local_names.feature_name
                    ?: (locationResponse[0].local_names.ascii ?: (locationResponse[0].name ?: ""))),
            country = locationResponse[0].country ?: "",
            state = locationResponse[0].state ?: "",
            added = added,
            current = current
        )
    }
}