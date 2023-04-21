package com.elijah.weather.data.mapper

import com.elijah.weather.data.database.LocationDbModel
import com.elijah.weather.data.network.model.LocationDto
import com.elijah.weather.domain.entity.City
import javax.inject.Inject

class LocationMapper @Inject constructor() {
    fun mapLocationDtoToCity(locationDto: LocationDto): City {
        return City(
            latitude = locationDto.lat ?: 0.0,
            longitude = locationDto.lon ?: 0.0,
            name = locationDto.local_names?.ru ?: locationDto.local_names?.feature_name
            ?: locationDto.local_names?.ascii ?: locationDto.name ?: "Не найдено",
            country = locationDto.country ?: "Не определена",
            state = locationDto.state ?: "Не определен",
            currentCity = false
        )
    }

    fun mapLocationDbModelToCity(locationDbModel: LocationDbModel): City {
        return City(
            id = locationDbModel.id,
            latitude = locationDbModel.latitude,
            longitude = locationDbModel.longitude,
            name = locationDbModel.cityName,
            country = locationDbModel.country,
            state = locationDbModel.state,
            currentCity = locationDbModel.currentCity
        )
    }

    fun mapCityToLocationDbModel(city: City): LocationDbModel {
        return LocationDbModel(
            id = city.id,
            cityName = city.name,
            country = city.country,
            state = city.state,
            latitude = city.latitude,
            longitude = city.longitude,
            currentCity = city.currentCity
        )
    }

    fun mapLocationCityDtoToCurrentLocationDbModel(
        locationDto: LocationDto,
        idCity: Int?
    ): LocationDbModel {
        return LocationDbModel(
            id = idCity ?: 0,
            latitude = locationDto.lat ?: 0.0,
            longitude = locationDto.lon ?: 0.0,
            cityName = locationDto.local_names?.ru ?: locationDto.local_names?.feature_name
            ?: locationDto.local_names?.ascii ?: locationDto.name ?: "Не найдено",
            country = locationDto.country ?: "Не определена",
            state = locationDto.state ?: "Не определен",
            currentCity = true
        )
    }
}