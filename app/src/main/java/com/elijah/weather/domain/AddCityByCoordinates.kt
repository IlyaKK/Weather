package com.elijah.weather.domain

import javax.inject.Inject

class AddCityByCoordinates @Inject constructor(
    private val repository: WeatherOfCityRepository
) {
    operator fun invoke(latitude:Double, longitude:Double){
        repository.addCityByCoordinates(
            latitude,
            longitude
        )
    }
}