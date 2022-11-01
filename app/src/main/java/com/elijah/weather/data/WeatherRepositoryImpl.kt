package com.elijah.weather.data

import com.elijah.weather.data.remote.RemoteWeatherDataSource
import com.elijah.weather.domain.WeatherRepository
import com.elijah.weather.domain.entity.DailyWeather
import com.elijah.weather.domain.entity.HourlyWeather
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class WeatherRepositoryImpl(private val remoteWeatherDataSource: RemoteWeatherDataSource) : WeatherRepository {
    override fun getHourlyWeather(
        latitude: Double,
        longitude: Double,
        units: String,
        language: String
    ): Flow<List<HourlyWeather>> {
        return flow {
            emit(remoteWeatherDataSource.getHourlyWeather(latitude, longitude, units, language))
        }
    }

    override fun getDailyWeather(
        latitude: Double,
        longitude: Double,
        units: String,
        language: String
    ): Flow<List<DailyWeather>> {
        return flow {
            emit(remoteWeatherDataSource.getDailyWeather(latitude, longitude, units, language))
        }
    }

}