package com.elijah.weather.presentation.mapper

import com.elijah.weather.domain.entity.City
import com.elijah.weather.presentation.slider_weather_of_cities.PointOfCityItem
import javax.inject.Inject

class PointsMapper @Inject constructor(){

    fun mapCityToPointOfItem(city: City):PointOfCityItem{
        return PointOfCityItem(
            id = city.id,
            current = city.currentCity
        )
    }
}