package com.elijah.weather.domain

import com.elijah.weather.domain.entity.City
import com.elijah.weather.domain.entity.WeatherOfCity
import kotlinx.coroutines.flow.Flow

interface WeatherOfCityRepository {
    fun searchCitiesByName(nameCity: String): Flow<List<City>>
    val cityList: Flow<List<City>>
    fun getWeatherOfCity(city: City): Flow<WeatherOfCity>
    suspend fun addCity(city: City)
    fun addCityByCoordinates(latitude: Double, longitude: Double)
    suspend fun deleteCity(city: City)
    suspend fun startCurrentCityUpdate(): Boolean
    fun startUpdateWeatherData()
}