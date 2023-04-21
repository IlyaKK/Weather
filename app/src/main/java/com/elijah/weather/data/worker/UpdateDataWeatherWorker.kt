package com.elijah.weather.data.worker

import android.content.Context
import androidx.work.*
import com.elijah.weather.data.database.LocationDao
import com.elijah.weather.data.database.WeatherDao
import com.elijah.weather.data.mapper.WeatherMapper
import com.elijah.weather.data.network.ApiService
import kotlinx.coroutines.flow.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class UpdateDataWeatherWorker(
    context: Context,
    workerParameters: WorkerParameters,
    private val apiService: ApiService,
    private val weatherDao: WeatherDao,
    private val locationDao: LocationDao,
    private val weatherMapper: WeatherMapper
) : CoroutineWorker(context, workerParameters) {

    override suspend fun doWork(): Result {
        var result: Result = Result.success()
        try {
            locationDao.getAllLocations()
                .onEach {
                    it.forEach { city ->
                        val weather = apiService.getWeather(city.latitude, city.longitude)
                        if (weather.daily.isNotEmpty() && weather.hourly.isNotEmpty()) {
                            weatherDao.deleteWeatherDayOfCity(city.id)
                            weatherDao.deleteWeatherHoursOfCity(city.id)
                            weatherDao.insertWeatherCityOfHours(weather.hourly.map { weatherHourDto ->
                                weatherMapper.mapWeatherOfHoursDtoToWeatherOfHoursDbModel(
                                    weatherHourDto,
                                    city.id
                                )
                            })
                            weatherDao.insertWeatherCityOfDays(weather.daily.map { weatherDay ->
                                weatherMapper.mapWeatherOfDaysDtoToWeatherOfDaysDbModel(
                                    weatherDay,
                                    city.id
                                )
                            })
                        }
                    }
                }
                .catch {
                    result = Result.retry()
                }
                .cancellable()
                .first()
        } catch (e: NoSuchElementException) {
            result = Result.retry()
        }

        return result
    }

    class Factory @Inject constructor(
        private val apiService: ApiService,
        private val weatherDao: WeatherDao,
        private val locationDao: LocationDao,
        private val weatherMapper: WeatherMapper
    ) : ChildWorkerFactory {
        override fun create(
            context: Context,
            workerParameters: WorkerParameters
        ): ListenableWorker {
            return UpdateDataWeatherWorker(
                context,
                workerParameters,
                apiService,
                weatherDao,
                locationDao,
                weatherMapper
            )
        }
    }

    companion object {
        const val NAME = "UpdateDataWeatherWorker"

        fun makeRequest(): PeriodicWorkRequest {
            return PeriodicWorkRequestBuilder<UpdateDataWeatherWorker>(25, TimeUnit.MINUTES)
                .setConstraints(
                    Constraints(
                        requiredNetworkType = NetworkType.CONNECTED
                    )
                )
                .build()
        }
    }
}