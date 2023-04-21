package com.elijah.weather.domain

import com.elijah.weather.domain.entity.City
import com.elijah.weather.domain.entity.WeatherOfCity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetWeatherOfCityUseCase @Inject constructor(
    private val repository: WeatherOfCityRepository
) {

    operator fun invoke(city: City): Flow<WeatherOfCity> {
        return repository.getWeatherOfCity(city)
    }
}