package com.elijah.weather.data.worker

import android.content.Context
import androidx.work.*
import com.elijah.weather.data.database.LocationDao
import com.elijah.weather.data.mapper.LocationMapper
import com.elijah.weather.data.network.ApiService
import com.elijah.weather.data.worker.LoadNewWeatherCityWorker.Companion.KEY_ID_CITY
import javax.inject.Inject

class RefreshCurrentLocationWorker(
    context: Context,
    workerParameters: WorkerParameters,
    private val apiService: ApiService,
    private val locationDao: LocationDao,
    private val locationMapper: LocationMapper
) : CoroutineWorker(context, workerParameters) {
    override suspend fun doWork(): Result {
        val latitude = inputData.getDouble(KEY_LATITUDE, 0.0)
        val longitude = inputData.getDouble(KEY_LONGITUDE, 0.0)
        val listLocationDto = apiService.getCityByCoordinates(latitude, longitude)
        val oldCurrentCityDb = locationDao.getCurrentLocation()
        val newCurrentCityDb = locationMapper.mapLocationCityDtoToCurrentLocationDbModel(
            listLocationDto[0], oldCurrentCityDb?.id
        )
        oldCurrentCityDb?.let {
            locationDao.deleteLocation(it.id)
        }
        val idData = locationDao.insertLocation(
            newCurrentCityDb
        )
        val outputData = workDataOf(
            KEY_ID_CITY to idData.toInt(),
            LoadNewWeatherCityWorker.KEY_LONGITUDE to longitude,
            LoadNewWeatherCityWorker.KEY_LATITUDE to latitude
        )
        return Result.success(outputData)
    }

    class Factory @Inject constructor(
        private val apiService: ApiService,
        private val locationDao: LocationDao,
        private val locationMapper: LocationMapper
    ) : ChildWorkerFactory {
        override fun create(
            context: Context,
            workerParameters: WorkerParameters
        ): ListenableWorker {
            return RefreshCurrentLocationWorker(
                context,
                workerParameters,
                apiService,
                locationDao,
                locationMapper
            )
        }
    }

    companion object {
        const val NAME = "RefreshCurrentLocationWorker"
        private const val KEY_LONGITUDE = "KEY_LONGITUDE"
        private const val KEY_LATITUDE = "KEY_LATITUDE"

        fun makeRequest(latitude: Double, longitude: Double): OneTimeWorkRequest {
            val inputData = Data.Builder().apply {
                putDouble(KEY_LATITUDE, latitude)
                putDouble(KEY_LONGITUDE, longitude)
            }.build()
            return OneTimeWorkRequestBuilder<RefreshCurrentLocationWorker>()
                .setInputData(inputData)
                .setConstraints(
                    Constraints(
                        requiredNetworkType = NetworkType.CONNECTED
                    )
                )
                .build()
        }
    }

}