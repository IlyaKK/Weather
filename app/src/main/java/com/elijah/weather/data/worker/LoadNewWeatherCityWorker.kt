package com.elijah.weather.data.worker

import android.content.Context
import androidx.work.*
import com.elijah.weather.data.database.WeatherDao
import com.elijah.weather.data.mapper.WeatherMapper
import com.elijah.weather.data.network.ApiService
import javax.inject.Inject

class LoadNewWeatherCityWorker(
    context: Context,
    workerParameters: WorkerParameters,
    private val apiService: ApiService,
    private val weatherDao: WeatherDao,
    private val weatherMapper: WeatherMapper
) : CoroutineWorker(context, workerParameters) {
    override suspend fun doWork(): Result {
        val latitude = inputData.getDouble(KEY_LATITUDE, 0.0)
        val longitude = inputData.getDouble(KEY_LONGITUDE, 0.0)
        val cityId = inputData.getInt(KEY_ID_CITY, 0)
        val weather = apiService.getWeather(latitude, longitude)
        if (weather.daily.isNotEmpty() && weather.hourly.isNotEmpty()) {
            weatherDao.insertWeatherCityOfHours(weather.hourly.map { weatherHourDto ->
                weatherMapper.mapWeatherOfHoursDtoToWeatherOfHoursDbModel(
                    weatherHourDto,
                    cityId
                )
            })
            weatherDao.insertWeatherCityOfDays(weather.daily.map { weatherDay ->
                weatherMapper.mapWeatherOfDaysDtoToWeatherOfDaysDbModel(
                    weatherDay,
                    cityId
                )
            })
        }
        return Result.success()
    }

    class Factory @Inject constructor(
        private val apiService: ApiService,
        private val weatherDao: WeatherDao,
        private val weatherMapper: WeatherMapper
    ) : ChildWorkerFactory {
        override fun create(
            context: Context,
            workerParameters: WorkerParameters
        ): ListenableWorker {
            return LoadNewWeatherCityWorker(
                context,
                workerParameters,
                apiService,
                weatherDao,
                weatherMapper
            )
        }
    }

    companion object {
        const val KEY_LONGITUDE = "KEY_LONGITUDE"
        const val KEY_LATITUDE = "KEY_LATITUDE"
        const val KEY_ID_CITY = "KEY_ID_CITY"
        const val NAME = "LoadNewWeatherCityWorker"
        fun makeRequest(latitude: Double, longitude: Double, idCity: Int): OneTimeWorkRequest {
            val inputData = Data.Builder().apply {
                putDouble(KEY_LATITUDE, latitude)
                putDouble(KEY_LONGITUDE, longitude)
                putInt(KEY_ID_CITY, idCity)
            }.build()
            return OneTimeWorkRequestBuilder<LoadNewWeatherCityWorker>()
                .setInputData(inputData)
                .setConstraints(
                    createConstraints()
                )
                .build()
        }

        fun makeRequest(): OneTimeWorkRequest {
            return OneTimeWorkRequestBuilder<LoadNewWeatherCityWorker>()
                .setConstraints(
                    createConstraints()
                )
                .build()
        }

        private fun createConstraints(): Constraints {
            return Constraints(
                requiredNetworkType = NetworkType.CONNECTED
            )
        }
    }
}