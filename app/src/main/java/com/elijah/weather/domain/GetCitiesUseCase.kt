package com.elijah.weather.domain

import com.elijah.weather.domain.entity.City
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCitiesUseCase @Inject constructor(
    private val repository: WeatherOfCityRepository
){

    operator fun invoke(): Flow<List<City>> {
        return repository.cityList
    }
}