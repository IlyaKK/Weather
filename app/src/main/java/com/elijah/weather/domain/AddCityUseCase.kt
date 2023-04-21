package com.elijah.weather.domain

import com.elijah.weather.domain.entity.City
import javax.inject.Inject

class AddCityUseCase @Inject constructor(
    private val repository: WeatherOfCityRepository
) {
    suspend operator fun invoke(city: City){
        repository.addCity(city)
    }
}