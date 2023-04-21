package com.elijah.weather.domain

import javax.inject.Inject

class StartCurrentCityUpdateUseCase @Inject constructor(
    private val repository: WeatherOfCityRepository
) {
    suspend operator fun invoke(): Boolean {
        return repository.startCurrentCityUpdate()
    }
}