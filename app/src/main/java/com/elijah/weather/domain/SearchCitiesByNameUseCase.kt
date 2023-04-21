package com.elijah.weather.domain

import com.elijah.weather.domain.entity.City
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchCitiesByNameUseCase @Inject constructor(
    private val repository: WeatherOfCityRepository
) {
    operator fun invoke(nameCity: String): Flow<List<City>> {
        return repository.searchCitiesByName(nameCity)
    }
}