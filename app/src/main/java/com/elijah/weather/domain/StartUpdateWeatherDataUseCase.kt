package com.elijah.weather.domain

import javax.inject.Inject

class StartUpdateWeatherDataUseCase @Inject constructor(
    private val repository: WeatherOfCityRepository
) {
    operator fun invoke() {
        repository.startUpdateWeatherData()
    }
}