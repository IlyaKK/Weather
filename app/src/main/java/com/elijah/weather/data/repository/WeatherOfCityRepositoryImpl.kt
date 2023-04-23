package com.elijah.weather.data.repository

import android.app.Application
import android.util.Log
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.WorkManager
import com.elijah.weather.data.database.LocationDao
import com.elijah.weather.data.database.WeatherDao
import com.elijah.weather.data.mapper.LocationMapper
import com.elijah.weather.data.mapper.WeatherMapper
import com.elijah.weather.data.network.ApiService
import com.elijah.weather.data.worker.LoadNewWeatherCityWorker
import com.elijah.weather.data.worker.RefreshCurrentLocationWorker
import com.elijah.weather.data.worker.UpdateDataWeatherWorker
import com.elijah.weather.domain.WeatherOfCityRepository
import com.elijah.weather.domain.entity.City
import com.elijah.weather.domain.entity.WeatherOfCity
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class WeatherOfCityRepositoryImpl @Inject constructor(
    private val application: Application,
    private val weatherDao: WeatherDao,
    private val locationDao: LocationDao,
    private val locationManager: LocationManager,
    private val apiService: ApiService,
    private val locationMapper: LocationMapper,
    private val weatherMapper: WeatherMapper
) : WeatherOfCityRepository {

    private val job = SupervisorJob()
    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Log.e("WeatherOfCityRepositoryImpl", throwable.stackTraceToString())
    }
    private val coroutineScope =
        CoroutineScope(Dispatchers.Default + job + coroutineExceptionHandler)

    private val mapWeatherCityFlows = mutableMapOf<Int, Flow<WeatherOfCity>>()
    override fun searchCitiesByName(nameCity: String): Flow<List<City>> {
        return flow {
            val list = apiService.getCitiesByName(nameCity = nameCity)
            emit(list.map { locationDto -> locationMapper.mapLocationDtoToCity(locationDto) })
        }
    }

    override val cityList: Flow<List<City>> = locationDao.getAllLocations()
        .filter { it.isNotEmpty() && it[0].currentCity }
        .map {
            it.map { locationDbModel -> locationMapper.mapLocationDbModelToCity(locationDbModel) }
        }
        .onEach { listCities ->
            val listIds = listCities.map { it.id }.toSet()
            mapWeatherCityFlows.entries.removeIf {
                !listIds.contains(it.key)
            }
        }
        .shareIn(
            coroutineScope, SharingStarted.Lazily, replay = 1
        )

    override fun getWeatherOfCity(city: City): Flow<WeatherOfCity> {
        return if (mapWeatherCityFlows.containsKey(city.id)) {
            mapWeatherCityFlows[city.id]!!
        } else {
            val flowWeatherHoursOfCity =
                weatherDao.getHoursWeatherByIdCity(city.id)
                    .filter { it.isNotEmpty() }
                    .shareIn(coroutineScope, SharingStarted.Lazily, replay = 1)
            val flowWeatherDaysOfCity =
                weatherDao.getDaysWeatherByIdCity(city.id)
                    .filter { it.isNotEmpty() }
                    .shareIn(coroutineScope, SharingStarted.Lazily, replay = 1)
            val flowAllWeather =
                flowWeatherHoursOfCity.zip(flowWeatherDaysOfCity) { flowHours, flowDays ->
                    WeatherOfCity(
                        hoursWeather = flowHours.map {
                            weatherMapper.mapWeatherOfHoursDbModelToHourlyWeather(
                                it
                            )
                        }, daysWeather = flowDays.map {
                            weatherMapper.mapWeatherOfDaysDbModelToDailyWeather(
                                it
                            )
                        })
                }.shareIn(coroutineScope, SharingStarted.Lazily, replay = 1)
            mapWeatherCityFlows[city.id] = flowAllWeather
            mapWeatherCityFlows[city.id]!!
        }
    }

    override suspend fun addCity(city: City) {
        val idCity = locationDao.insertLocation(locationMapper.mapCityToLocationDbModel(city))
        val workManager = WorkManager.getInstance(application)
        workManager.enqueueUniqueWork(
            LoadNewWeatherCityWorker.NAME,
            ExistingWorkPolicy.APPEND,
            LoadNewWeatherCityWorker.makeRequest(city.latitude, city.longitude, idCity.toInt())
        )
    }

    override fun addCityByCoordinates(latitude: Double, longitude: Double) {
        val workManager = WorkManager.getInstance(application)
        val addWeatherCity = LoadNewWeatherCityWorker.makeRequest()
        workManager
            .beginUniqueWork(
                RefreshCurrentLocationWorker.NAME,
                ExistingWorkPolicy.KEEP,
                RefreshCurrentLocationWorker.makeRequest(latitude, longitude)
            )
            .then(addWeatherCity)
            .enqueue()
    }

    override suspend fun deleteCity(city: City) {
        locationDao.deleteLocation(city.id)
    }

    override suspend fun startCurrentCityUpdate(): Boolean {
        return locationManager.startLocationUpdates()
    }

    override fun startUpdateWeatherData() {
        val workManager = WorkManager.getInstance(application)
        workManager.enqueueUniquePeriodicWork(
            UpdateDataWeatherWorker.NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            UpdateDataWeatherWorker.makeRequest()
        )

    }
}

